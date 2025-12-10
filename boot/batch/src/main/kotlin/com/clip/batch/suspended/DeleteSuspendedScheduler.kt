package com.clip.batch.suspended

import com.clip.batch.suspended.tasklet.DeleteSuspendedTasklet
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
class DeleteSuspendedScheduler(
    private val jdbcTemplate: JdbcTemplate,
    private val jobOperator: JobOperator,
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
) {

    @Scheduled(cron = "0 30 4 * * ?")
    fun runDeleteSuspendedJob() {
//        val jobParameters = JobParametersBuilder(jobExplorer)
//            .getNextJobParameters(deleteSuspendedJob())
//            .toJobParameters()
//        jobLauncher.run(deleteSuspendedJob(), jobParameters)

        jobOperator.startNextInstance(deleteSuspendedJob())
    }
    @Bean
    fun deleteSuspendedJob(): Job =
        JobBuilder("deleteSuspendedJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start( deleteSuspendedStep())
            .build()

    @Bean
    fun deleteSuspendedStep(): Step =
        StepBuilder("deleteSuspendedStep", jobRepository)
            .tasklet(DeleteSuspendedTasklet(jdbcTemplate), transactionManager)
            .build()
}