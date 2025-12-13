package com.clip.batch.card

import com.clip.batch.card.dto.CommentRelatedEntitiesDeletionDto
import com.clip.batch.card.dto.FeedRelatedEntitiesDeletionDto
import com.clip.batch.card.repository.*
import com.clip.data.card.entity.CommentCard
import com.clip.data.card.entity.FeedCard
import jakarta.persistence.EntityManager
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.job.parameters.JobParametersBuilder
import org.springframework.batch.core.launch.JobOperator
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.Step
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.infrastructure.item.ItemProcessor
import org.springframework.batch.infrastructure.item.ItemWriter
import org.springframework.batch.infrastructure.item.database.JdbcPagingItemReader
import org.springframework.batch.infrastructure.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.batch.infrastructure.item.database.support.SqlPagingQueryProviderFactoryBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.PlatformTransactionManager
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.sql.DataSource

@Configuration
class DeleteStoryCardScheduler(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val jobOperator: JobOperator,
    private val dataSource: DataSource,
    private val entityManager: EntityManager,

    private val commentCardBatchRepository: CommentCardBatchRepository,
    private val commentLikeBatchRepository: CommentLikeBatchRepository,
    private val commentReportBatchRepository: CommentReportBatchRepository,
    private val feedCardBatchRepository: FeedCardBatchRepository,
    private val feedLikeBatchRepository: FeedLikeBatchRepository,
    private val feedReportBatchRepository: FeedReportBatchRepository,
    private val cardImgBatchRepository: CardImgBatchRepository,
    private val notificationBatchRepository: NotificationBatchRepository,
    private val commentTagBatchRepository: CommentTagBatchRepository,
    private val feedTagBatchRepository: FeedTagBatchRepository,

    private val deletedStoryFeedIdReader: JdbcPagingItemReader<Long>,
) {

    companion object {
        private const val CHUNK_SIZE = 100
    }

    @Scheduled(cron = "0 0 4 * * ?")
    fun runJob() {
        val baseTime = LocalDateTime.now().minusDays(1)
        val jobParameters = JobParametersBuilder()
            .addLong("run.id", System.currentTimeMillis())
            .addString("baseTime", baseTime.toString())
            .toJobParameters()

        jobOperator.start(commentCardReaderJob(), jobParameters)
    }

    @Bean
    fun commentCardReaderJob(): Job =
        JobBuilder("commentCardReaderJob", jobRepository)
            .start(deletedStoryCommentCardStep())
            .on(ExitStatus.COMPLETED.exitCode)
            .to(deletedStoryFeedCardStep())
            .from(deletedStoryFeedCardStep())
            .end()
            .build()

    @Bean
    fun deletedStoryCommentCardStep(): Step =
        StepBuilder("deletedStoryCommentCardStep", jobRepository)
            .chunk<Long, CommentRelatedEntitiesDeletionDto>(CHUNK_SIZE)
            .transactionManager(transactionManager)
            .reader(deletedStoryFeedIdReader)
            .processor(commentRelatedEntitiesDeletionProcessor())
            .writer(commentRelatedEntitiesDeletionWriter())
            .build()

    @Bean
    fun deletedStoryFeedCardStep(): Step =
        StepBuilder("deletedStoryFeedCardStep", jobRepository)
            .chunk<Long, FeedRelatedEntitiesDeletionDto>(CHUNK_SIZE)
            .transactionManager(transactionManager)
            .reader(deletedStoryFeedIdReader)
            .processor(feedRelatedEntitiesDeletionProcessor())
            .writer(feedDeletionWriter())
            .build()

    @Bean
    @StepScope
    fun deletedStoryFeedIdReader(
        @Value("#{jobParameters['baseTime']}") baseTime: String
    ): JdbcPagingItemReader<Long> {
        val parsedBaseTime = LocalDateTime.parse(baseTime)

        val parameterValues = mapOf(
            "isStory" to true,
            "baseTime" to Timestamp.valueOf(parsedBaseTime)
        )

        val queryProviderFactory = SqlPagingQueryProviderFactoryBean().apply {
            setDataSource(dataSource)
            setSelectClause("SELECT pk")
            setFromClause("FROM feed_card")
            setWhereClause("WHERE is_story = :isStory AND created_at < :baseTime")
            setSortKey("pk")
        }

        val queryProvider = queryProviderFactory.`object`

        return JdbcPagingItemReaderBuilder<Long>()
            .name("deletedStoryFeedIdReader")
            .dataSource(dataSource)
            .pageSize(CHUNK_SIZE)
            .queryProvider(queryProvider)
            .parameterValues(parameterValues)
            .rowMapper { rs, _ -> rs.getLong("pk") }
            .build()
    }

    @Bean
    fun commentRelatedEntitiesDeletionProcessor():
            ItemProcessor<Long, CommentRelatedEntitiesDeletionDto> =
        ItemProcessor { feedCardId ->
            feedCardBatchRepository.findByIdOrNull(feedCardId)?.let { feedCard: FeedCard ->
                val commentCardsForDeletion: List<CommentCard> =
                    commentCardBatchRepository.findCommentCardsForDeletion(feedCard.pk)
                cardImgBatchRepository.updateCommentCardImgNull(commentCardsForDeletion)

                val commentTagsForDeletion =
                    commentTagBatchRepository.findCommentTagsForDeletion(commentCardsForDeletion)
                val commentLikesForDeletion =
                    commentLikeBatchRepository.findCommentLikesForDeletion(commentCardsForDeletion)
                val commentReportsForDeletion =
                    commentReportBatchRepository.findCommentReportsForDeletion(commentCardsForDeletion)

                val commentCardIds = commentCardsForDeletion.map { card -> card.pk }
                val notificationsForDeletion =
                    notificationBatchRepository.findNotificationsForDeletion(commentCardIds)

                CommentRelatedEntitiesDeletionDto(
                    commentCards = commentCardsForDeletion,
                    commentLikes = commentLikesForDeletion,
                    commentReports = commentReportsForDeletion,
                    notifications = notificationsForDeletion,
                    commentTags = commentTagsForDeletion,
                )
            }
        }

    @Bean
    fun feedRelatedEntitiesDeletionProcessor():
            ItemProcessor<Long, FeedRelatedEntitiesDeletionDto> =
        ItemProcessor { feedCardId ->
            feedCardBatchRepository.findByIdOrNull(feedCardId)?.let { feedCard: FeedCard ->
                val feedLikesForDeletion =
                    feedLikeBatchRepository.findFeedLikesForDeletion(feedCard)
                val feedReportsForDeletion =
                    feedReportBatchRepository.findFeedReportsForDeletion(feedCard)
                val feedTagsForDeletion = feedTagBatchRepository.findFeedTagsForDeletion(feedCard)

                FeedRelatedEntitiesDeletionDto(
                    feedCard = feedCard,
                    feedLikes = feedLikesForDeletion,
                    feedReports = feedReportsForDeletion,
                    feedTags = feedTagsForDeletion
                )
            }
        }

    @Bean
    fun commentRelatedEntitiesDeletionWriter():
            ItemWriter<CommentRelatedEntitiesDeletionDto> =
        ItemWriter { items ->
            items.forEach { dto ->
                commentTagBatchRepository.deleteAllInBatch(dto.commentTags)
                commentLikeBatchRepository.deleteAllInBatch(dto.commentLikes)
                commentReportBatchRepository.deleteAllInBatch(dto.commentReports)
                notificationBatchRepository.deleteAllInBatch(dto.notifications)
                commentCardBatchRepository.deleteAllInBatch(dto.commentCards)
            }
        }

    @Bean
    fun feedDeletionWriter():
            ItemWriter<FeedRelatedEntitiesDeletionDto> =
        ItemWriter { items ->
            items.filterNotNull().forEach { dto ->
                val feedCard = dto.feedCard

                cardImgBatchRepository.updateFeedCardImgNull(feedCard)
                feedLikeBatchRepository.deleteAllInBatch(dto.feedLikes)
                feedReportBatchRepository.deleteAllInBatch(dto.feedReports)
                feedTagBatchRepository.deleteAllInBatch(dto.feedTags)

                val notificationsForDeletion =
                    notificationBatchRepository.findNotificationsForDeletion(listOf(feedCard.pk))
                notificationBatchRepository.deleteAllInBatch(notificationsForDeletion)
                entityManager.flush()
                entityManager.clear()

                feedCardBatchRepository.delete(feedCard)
            }
        }

}

