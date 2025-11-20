package com.clip.batch.img

import com.clip.batch.img.tasklet.DeleteCardImgTasklet
import org.slf4j.LoggerFactory
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
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class DeleteCardImgScheduler(
    private val jobExplorer: JobExplorer,
    private val jobLauncher: JobLauncher,
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val deleteCardImgTasklet: DeleteCardImgTasklet
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "0 0 3 * * *")
    fun runJob() {
        val jobParameters = JobParametersBuilder(jobExplorer)
            .getNextJobParameters(deleteCardImgJob())
            .toJobParameters()

        runCatching {
            jobLauncher.run(deleteCardImgJob(), jobParameters)
        }.onFailure { e ->
            log.error("Failed to execute DeleteCardImg batch job", e)
        }
    }

    @Bean
    fun deleteCardImgJob(): Job = JobBuilder("DeleteCardImgBatchJob", jobRepository)
        .incrementer(RunIdIncrementer())
        .start(deleteCardImgStep())
        .build()

    @Bean
    fun deleteCardImgStep(): Step = StepBuilder("DeleteCardImgStep", jobRepository)
        .tasklet(deleteCardImgTasklet, transactionManager)
        .build()
}