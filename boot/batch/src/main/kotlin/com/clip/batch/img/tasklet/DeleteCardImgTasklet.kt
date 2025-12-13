package com.clip.batch.img.tasklet

import com.clip.infra.s3.S3ImgService
import org.slf4j.LoggerFactory
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.StepContribution
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.infrastructure.repeat.RepeatStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class DeleteCardImgTasklet(
    private val jdbcTemplate: JdbcTemplate,
    private val s3ImgService: S3ImgService
) : Tasklet {

    companion object {
        private val log = LoggerFactory.getLogger(DeleteCardImgTasklet::class.java)
        private const val USER_CARD_IMG = "card/user/"
    }

    override fun execute(
        contribution: StepContribution,
        chunkContext: ChunkContext
    ): RepeatStatus {
        log.info("Starting DeleteCardImgTasklet execution")

        val imgNames = jdbcTemplate.queryForList(
            "select img_name from card_img where feed_card is null and comment_card is null",
            String::class.java
        )

        if (imgNames.isEmpty()) {
            log.info("No unused card images found")
            return RepeatStatus.FINISHED
        }

        try {
            s3ImgService.deleteImgs(USER_CARD_IMG, imgNames)
            log.info("Successfully deleted ${imgNames.size} images from S3")
        } catch (e: Exception) {
            log.error("Failed to delete images from S3. Rolling back transaction.", e)
            throw e
        }

        log.info("Found ${imgNames.size} unused card images to delete")

        val deletedCount = jdbcTemplate.update(
            "delete from card_img where feed_card is null and comment_card is null"
        )

        contribution.incrementWriteCount(deletedCount.toLong())
        log.info("${deletedCount}개의 미사용 카드 이미지가 삭제되었습니다.")

        return RepeatStatus.FINISHED
    }
}
