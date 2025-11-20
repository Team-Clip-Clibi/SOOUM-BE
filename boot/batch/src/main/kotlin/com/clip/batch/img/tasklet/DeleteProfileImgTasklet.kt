package com.clip.batch.img.tasklet

import com.clip.infra.s3.S3ImgService
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class DeleteProfileImgTasklet(
    private val jdbcTemplate: JdbcTemplate,
    private val s3ImgService: S3ImgService
) : Tasklet {

    companion object {
        private val log = LoggerFactory.getLogger(DeleteProfileImgTasklet::class.java)
        private const val PROFILE_IMG = "profile/"
    }

    override fun execute(
        contribution: StepContribution,
        chunkContext: ChunkContext
    ): RepeatStatus {
        log.info("Starting DeleteProfileImgTasklet execution")

        val imgNames = jdbcTemplate.queryForList(
            "select img_name from profile_img where profile_owner is null",
            String::class.java
        )

        if (imgNames.isEmpty()) {
            log.info("No unused profile images found")
            return RepeatStatus.FINISHED
        }

        log.info("Found ${imgNames.size} unused profile images to delete")

        try {
            s3ImgService.deleteImgs(PROFILE_IMG, imgNames)
            log.info("Successfully deleted ${imgNames.size} images from S3")
        } catch (e: Exception) {
            log.error("Failed to delete images from S3(DB already deleted)", e)
        }

        val deletedCount = jdbcTemplate.update(
            "delete from profile_img where profile_owner is null"
        )

        contribution.incrementWriteCount(deletedCount.toLong())
        log.info("${deletedCount}개의 미사용 프로필 이미지가 DB에서 삭제되었습니다.")

        return RepeatStatus.FINISHED
    }
}
