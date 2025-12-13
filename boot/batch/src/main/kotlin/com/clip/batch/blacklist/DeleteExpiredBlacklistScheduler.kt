package com.clip.batch.blacklist

import com.clip.batch.blacklist.tasklet.DeleteExpiredBlacklistTasklet
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.job.parameters.RunIdIncrementer
import org.springframework.batch.core.launch.JobOperator
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.Step
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.PlatformTransactionManager


@Configuration
class DeleteExpiredBlacklistScheduler(
    private val jobOperator: JobOperator,
    private val jobRepository: JobRepository,
    private val jdbcTemplate: JdbcTemplate,
    private val transactionManager: PlatformTransactionManager,
) {

    @Scheduled(cron = "0 45 4 * * ?")
    fun runDeleteExpiredBlacklistJob() {
        jobOperator.startNextInstance(deleteExpiredBlacklistJob())
    }
    @Bean
    fun deleteExpiredBlacklistJob(): Job =
        JobBuilder("deleteExpiredBlacklistJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start( deleteExpiredBlacklistStep())
            .build()

    @Bean
    fun deleteExpiredBlacklistStep(): Step =
        StepBuilder("deleteExpiredBlacklistStep", jobRepository)
            .tasklet(DeleteExpiredBlacklistTasklet(jdbcTemplate), transactionManager)
            .build()
}