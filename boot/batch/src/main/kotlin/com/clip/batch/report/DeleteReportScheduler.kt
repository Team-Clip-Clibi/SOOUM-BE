package com.clip.batch.report

import com.clip.batch.report.tasklet.DeleteCommentReportTasklet
import com.clip.batch.report.tasklet.DeleteFeedReportTasklet
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
class DeleteReportScheduler(
    private val jobOperator: JobOperator,
    private val jobRepository: JobRepository,
    private val jdbcTemplate: JdbcTemplate,
    private val transactionManager: PlatformTransactionManager,
) {

    @Scheduled(cron = "0 30 4 * * * ")
    fun runDeleteReportJob() {
//        val jobParameters = JobParametersBuilder(jobExplorer)
//            .getNextJobParameters(deleteReportAfter6monthJob())
//            .toJobParameters()
//        jobLauncher.run(deleteReportAfter6monthJob(), jobParameters)
        jobOperator.startNextInstance(deleteReportAfter6monthJob())
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