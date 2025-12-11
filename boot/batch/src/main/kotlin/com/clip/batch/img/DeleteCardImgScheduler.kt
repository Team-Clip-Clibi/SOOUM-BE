package com.clip.batch.img

import com.clip.batch.img.tasklet.DeleteCardImgTasklet
import org.slf4j.LoggerFactory
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.job.parameters.RunIdIncrementer
import org.springframework.batch.core.launch.JobOperator
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.Step
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class DeleteCardImgScheduler(
    private val jobOperator: JobOperator,
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val deleteCardImgTasklet: DeleteCardImgTasklet
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "0 0 3 * * *")
    fun runJob() {
        runCatching {
            jobOperator.startNextInstance(deleteCardImgJob())
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