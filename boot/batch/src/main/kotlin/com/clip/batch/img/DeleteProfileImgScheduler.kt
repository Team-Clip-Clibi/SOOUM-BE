package com.clip.batch.img

import com.clip.batch.img.tasklet.DeleteProfileImgTasklet
import org.slf4j.LoggerFactory
import org.springframework.batch.core.*
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.PlatformTransactionManager


@Configuration
class DeleteProfileImgScheduler(
    private val jobExplorer: JobExplorer,
    private val jobLauncher: JobLauncher,
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val deleteProfileImgTasklet: DeleteProfileImgTasklet
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "0 30 3 * * *")
    fun runJob() {
        val jobParameters = JobParametersBuilder(jobExplorer)
            .getNextJobParameters(deleteProfileImgJob())
            .toJobParameters()

        runCatching {
            jobLauncher.run(deleteProfileImgJob(), jobParameters)
        }.onFailure { e ->
            log.error("Failed to execute DeleteProfileImg batch job", e)
        }
    }

    @Bean
    fun deleteProfileImgJob(): Job = JobBuilder("DeleteProfileImgBatchJob", jobRepository)
        .incrementer(RunIdIncrementer())
        .start(deleteProfileImgStep())
        .build()

    @Bean
    fun deleteProfileImgStep(): Step = StepBuilder("DeleteProfileImgStep", jobRepository)
        .tasklet(deleteProfileImgTasklet, transactionManager)
        .build()
}
