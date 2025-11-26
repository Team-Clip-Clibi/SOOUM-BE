package com.clip.batch.common

import com.clip.infra.slack.SlackEventPublisher
import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component

@Component
class GlobalBatchJobListener(
    private val slackEventPublisher: SlackEventPublisher
) : JobExecutionListener {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun beforeJob(jobExecution: JobExecution) {
        val jobName = jobExecution.jobInstance.jobName
        log.info("[$jobName] Batch job started - JobId: ${jobExecution.jobId}, Parameters: ${jobExecution.jobParameters}")
    }

    override fun afterJob(jobExecution: JobExecution) {
        val jobName = jobExecution.jobInstance.jobName
        val status = jobExecution.status
        val writeCount = jobExecution.stepExecutions.sumOf { it.writeCount }
        val readCount = jobExecution.stepExecutions.sumOf { it.readCount }

        log.info("[$jobName] Batch job finished - Status: $status, Read: $readCount, Write: $writeCount")

        when (status) {
            BatchStatus.COMPLETED -> {
                log.info("[$jobName] Successfully completed")
            }
            BatchStatus.FAILED -> {
                log.error("[$jobName] Failed! Sending Slack notification...")
                slackEventPublisher.publish()
            }
            BatchStatus.STOPPED -> {
                log.warn("[$jobName] Stopped")
                slackEventPublisher.publish(jobExecution)
            }
            else -> {
                log.warn("[$jobName] Ended with unexpected status: $status")
            }
        }
    }
}
