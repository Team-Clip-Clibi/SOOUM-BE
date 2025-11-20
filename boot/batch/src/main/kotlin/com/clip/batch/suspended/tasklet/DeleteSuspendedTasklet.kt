package com.clip.batch.suspended.tasklet

import com.clip.batch.blacklist.tasklet.DeleteExpiredBlacklistTasklet
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.jdbc.core.JdbcTemplate

class DeleteSuspendedTasklet(
    val jdbcTemplate: JdbcTemplate
): Tasklet {
    companion object {
        private val log = LoggerFactory.getLogger(DeleteExpiredBlacklistTasklet::class.java)
    }

    override fun execute(
        contribution: StepContribution,
        chunkContext: ChunkContext
    ): RepeatStatus {

        val updateCnt = jdbcTemplate.update(
            "delete from Suspended s where s.until_ban < current_timestamp"
        )
        log.info("${updateCnt}개의 기간 만료된 탈퇴 이력(재가입 방지용) 레코드가 삭제되었습니다.")
        return RepeatStatus.FINISHED
    }


}