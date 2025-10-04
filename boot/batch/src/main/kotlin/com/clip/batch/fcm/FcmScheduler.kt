package com.clip.batch.fcm

import com.clip.batch.fcm.service.FcmSchedulerService
import com.clip.data.notification.service.FcmSchedulerContentService
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.Order
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class FcmScheduler(
    private val dataSource: DataSource,
    private val jobExplorer: JobExplorer,
    private val jobLauncher: JobLauncher,
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val fcmSchedulerContentService: FcmSchedulerContentService,
    private val fcmSchedulerService: FcmSchedulerService,
) {
    companion object {
        private const val CHUNK_SIZE = 200
    }

    @Scheduled(cron = "0 0 21 * * *")
    fun runFcmSchedulerJob() {

        val findFirstSchedulerContent = fcmSchedulerContentService.findFirstSchedulerContent()

        val jobParameters = JobParametersBuilder(jobExplorer)
            .getNextJobParameters(fcmSchedulerJob())
            .addString("title", findFirstSchedulerContent.title)
            .addString("content", findFirstSchedulerContent.content)
            .toJobParameters()
        jobLauncher.run(fcmSchedulerJob(),jobParameters)
    }

    @Bean
    fun fcmSchedulerJob(): Job =
        JobBuilder("fcmSchedulerJob",jobRepository)
            .incrementer(RunIdIncrementer())
            .start(fcmSchedulerStep())
            .build()


    @Bean
    fun fcmSchedulerStep(): Step =
        StepBuilder("fcmSchedulerStep", jobRepository)
            .chunk<String, String>(CHUNK_SIZE, transactionManager)
            .reader(fcmReader())
            .writer(fcmWriter(null, null))
            .build()

    @Bean
    fun fcmReader(): JdbcPagingItemReader<String> =
        JdbcPagingItemReaderBuilder<String>()
            .name("fcmReader")
            .dataSource(dataSource)
            .pageSize(CHUNK_SIZE)
            .selectClause("select firebaseToken")
            .fromClause("from Member")
            .whereClause("isAllowNotify = true and firebaseToken is not null")
            .sortKeys(mapOf("pk" to Order.ASCENDING))
            .rowMapper { rs, _ -> rs.getString("firebaseToken") }
            .build()

    @Bean
    @JobScope
    fun fcmWriter(
        @Value("#{jobParameters['title']}") title: String?,
        @Value("#{jobParameters['content']}") content: String?,
    ): ItemWriter<String> = ItemWriter { chunk ->
        val message = MulticastMessage.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(title)
                    .setBody(content)
                    .build()
            )
            .putAllData(mapOf("notificationType" to  "feed"))
            .addAllTokens(chunk.items.toList())
            .build()

        fcmSchedulerService.sendMulticastFcm(message)
    }
}