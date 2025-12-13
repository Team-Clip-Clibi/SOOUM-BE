package com.clip.batch.blacklist.tasklet

import org.slf4j.LoggerFactory
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.StepContribution
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.infrastructure.repeat.RepeatStatus
import org.springframework.jdbc.core.JdbcTemplate

class DeleteExpiredBlacklistTasklet(
    val jdbcTemplate: JdbcTemplate,
): Tasklet {
    companion object {
        private val log = LoggerFactory.getLogger(DeleteExpiredBlacklistTasklet::class.java)
    }
    override fun execute(
        contribution: StepContribution,
        chunkContext: ChunkContext
    ): RepeatStatus {
        val updateCnt = jdbcTemplate.update(
            "delete from blacklist b where b.expired_at < current_timestamp "
        )
        log.info("${updateCnt}개의 기간 만료된 블랙리스트 토큰 레코드가 삭제되었습니다.")
        return RepeatStatus.FINISHED
    }
}