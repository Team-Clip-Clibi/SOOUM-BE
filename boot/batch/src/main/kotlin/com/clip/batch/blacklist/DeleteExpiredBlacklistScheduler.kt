package com.clip.batch.blacklist

import com.clip.batch.blacklist.tasklet.DeleteExpiredBlacklistTasklet
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.Step
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.PlatformTransactionManager


@Configuration
class DeleteExpiredBlacklistScheduler(
    private val jobExplorer: JobExplorer,
    private val jobLauncher: JobLauncher,
    private val jobRepository: JobRepository,
    private val jdbcTemplate: JdbcTemplate,
    private val transactionManager: PlatformTransactionManager,
) {

    @Scheduled(cron = "0 45 4 * * ?")
    fun runDeleteExpiredBlacklistJob() {
        val jobParameters = JobParametersBuilder(jobExplorer)
            .getNextJobParameters(deleteExpiredBlacklistJob())
            .toJobParameters()
        jobLauncher.run(deleteExpiredBlacklistJob(), jobParameters)
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