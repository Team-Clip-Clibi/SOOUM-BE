package com.clip.batch.report

import com.clip.batch.report.tasklet.DeleteCommentReportTasklet
import com.clip.batch.report.tasklet.DeleteFeedReportTasklet
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
class DeleteReportScheduler(
    private val jobExplorer: JobExplorer,
    private val jobLauncher: JobLauncher,
    private val jobRepository: JobRepository,
    private val jdbcTemplate: JdbcTemplate,
    private val transactionManager: PlatformTransactionManager,
) {

    @Scheduled(cron = "0 30 4 * * * ")
    fun runDeleteReportJob() {
        val jobParameters = JobParametersBuilder(jobExplorer)
            .getNextJobParameters(deleteReportAfter6monthJob())
            .toJobParameters()
        jobLauncher.run(deleteReportAfter6monthJob(), jobParameters)
    }
    @Bean
    fun deleteReportAfter6monthJob(): Job =
        JobBuilder("deleteReportAfter6monthJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(deleteFeedReportStep())
            .next(deleteCommentReportStep())
            .build()

    @Bean
    fun deleteFeedReportStep(): Step =
        StepBuilder("deleteFeedReportStep", jobRepository)
            .tasklet(DeleteFeedReportTasklet(jdbcTemplate), transactionManager)
            .build()

    @Bean
    fun deleteCommentReportStep(): Step =
        StepBuilder("deleteCommentReportStep", jobRepository)
            .tasklet(DeleteCommentReportTasklet(jdbcTemplate), transactionManager)
            .build()

}