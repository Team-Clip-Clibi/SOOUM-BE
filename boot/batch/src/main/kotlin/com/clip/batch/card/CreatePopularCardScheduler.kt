//package com.clip.batch.card
//
//import com.github.f4b6a3.tsid.TsidCreator
//import org.springframework.batch.core.configuration.annotation.StepScope
//import org.springframework.batch.core.job.Job
//import org.springframework.batch.core.job.builder.JobBuilder
//import org.springframework.batch.core.job.parameters.JobParametersBuilder
//import org.springframework.batch.core.job.parameters.RunIdIncrementer
//import org.springframework.batch.core.launch.JobOperator
//import org.springframework.batch.core.repository.JobRepository
//import org.springframework.batch.core.step.Step
//import org.springframework.batch.core.step.builder.StepBuilder
//import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader
//import org.springframework.batch.infrastructure.item.database.JdbcPagingItemReader
//import org.springframework.batch.infrastructure.item.database.Order
//import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder
//import org.springframework.batch.infrastructure.item.database.builder.JdbcCursorItemReaderBuilder
//import org.springframework.batch.infrastructure.item.database.builder.JdbcPagingItemReaderBuilder
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
//import org.springframework.scheduling.annotation.Scheduled
//import org.springframework.transaction.PlatformTransactionManager
//import java.util.*
//import javax.sql.DataSource
//
//@Configuration
//class CreatePopularCardScheduler(
//    private val dataSource: DataSource,
//    private val jobOperator: JobOperator,
//    private val jobRepository: JobRepository,
//    private val transactionManager: PlatformTransactionManager,
//) {
//
//    companion object {
//        private const val CHUNK_SIZE = 100
//    }
//
//    @Scheduled(cron = "0 */10 * * * *")
//    fun runCreatePopularCardJob() {
//        val jobParameters = JobParametersBuilder()
//            .addString("createVersion", UUID.randomUUID().toString(), false)
//            .toJobParameters()
////        jobLauncher.run(createPopularCardJob(), jobParameters)
//        jobOperator.start(createPopularCardJob(), jobParameters)
//    }
//
//    @Bean
//    fun createPopularCardJob(): Job =
//        JobBuilder("createPopularCardJob", jobRepository)
//            .incrementer(RunIdIncrementer())
//            .start( createNewPopularCardStep())
//            .on("COMPLETED")
//            .to(deletePreviousPopularCardStep())
//            .end()
//            .build()
//
//    @Bean
//    fun createNewPopularCardStep(): Step =
//        StepBuilder("createNewPopularCardStep", jobRepository)
//            .chunk<PopularFeedCardId, PopularFeedCardId>(CHUNK_SIZE)
//            .transactionManager(transactionManager)
//            .reader(newPopularCardReader())
//            .writer(newPopularCardWriter(null))
//            .build()
//
//    @Bean
//    fun deletePreviousPopularCardStep(): Step =
//        StepBuilder("deletePreviousPopularCardStep", jobRepository)
//            .chunk<PopularFeedCardId, PopularFeedCardId>(CHUNK_SIZE)
//            .transactionManager(transactionManager)
//            .reader(deletePreviousPopularCardReader(null))
//            .writer(deletePreviousPopularCardWriter())
//            .build()
//
//    @Bean
//    fun newPopularCardReader(): JdbcCursorItemReader<PopularFeedCardId> =
//        JdbcCursorItemReaderBuilder<PopularFeedCardId>()
//            .name("newPopularCardReader")
//            .dataSource(dataSource)
//            .sql("""
//                SELECT
//                    fc.pk AS id
//                FROM feed_card fc
//                LEFT JOIN (
//                    SELECT fv.target_feed AS feed_id, COUNT(*) AS views_7d
//                    FROM feed_view fv
//                    WHERE fv.visit_date >= CURRENT_DATE - INTERVAL 7 DAY
//                    GROUP BY fv.target_feed
//                ) v ON v.feed_id = fc.pk
//                LEFT JOIN (
//                    SELECT fl.target_card AS feed_id, COUNT(*) AS likes_cnt
//                    FROM feed_like fl
//                    WHERE fl.is_deleted = FALSE
//                    GROUP BY fl.target_card
//                ) l ON l.feed_id = fc.pk
//                WHERE fc.is_feed_active = TRUE
//                    AND fc.is_deleted = FALSE
//                    AND (fc.is_story = FALSE OR (fc.is_story = TRUE AND fc.created_at > NOW() - INTERVAL 1 DAY))
//                ORDER BY (COALESCE(v.views_7d, 0) + COALESCE(l.likes_cnt, 0) * 5) DESC,
//                    fc.pk DESC
//                LIMIT 200
//                """)
//            .dataRowMapper(PopularFeedCardId::class.java)
//            .build()
//
//    @Bean
//    @StepScope
//    fun newPopularCardWriter(@Value("#{jobParameters['createVersion']}")  createVersion: String?) =
//        JdbcBatchItemWriterBuilder<PopularFeedCardId>()
//            .dataSource(dataSource)
//            .sql("""
//                INSERT INTO popular_feed (pk, POPULAR_CARD, CREATE_VERSION, created_at, updated_at)
//                VALUES (:pk, :id, :createVersion, NOW(), NOW())
//                """)
//            .itemSqlParameterSourceProvider { item ->
//                MapSqlParameterSource()
//                    .addValue("pk", TsidCreator.getTsid1024().toLong())
//                    .addValue("id", item.id)
//                    .addValue("createVersion", createVersion)
//            }
//            .assertUpdates(true)
//            .build()
//
//    @Bean
//    @StepScope
//    fun deletePreviousPopularCardReader(
//        @Value("#{jobParameters['createVersion']}") createVersion: Any
//    ): JdbcPagingItemReader<PopularFeedCardId> =
//        JdbcPagingItemReaderBuilder<PopularFeedCardId>()
//            .name("deletePreviousPopularCardReader")
//            .dataSource(dataSource)
//            .pageSize(CHUNK_SIZE)
//            .selectClause("SELECT pk AS id, pk")
//            .fromClause("FROM popular_feed")
//            .whereClause("""
//                WHERE CREATE_VERSION != :createVersion
//                """)
//            .parameterValues(mapOf("createVersion" to createVersion).toMutableMap())
//            .sortKeys(mapOf("pk" to Order.DESCENDING))
//            .dataRowMapper(PopularFeedCardId::class.java)
//            .build()
//
//    @Bean
//    fun deletePreviousPopularCardWriter() =
//        JdbcBatchItemWriterBuilder<PopularFeedCardId>()
//            .dataSource(dataSource)
//            .sql("""
//                DELETE FROM popular_feed WHERE pk = :id
//                """)
//            .beanMapped()
//            .assertUpdates(false)
//            .build()
//
//    data class PopularFeedCardId(
//        val id: Long,
//    )
//}