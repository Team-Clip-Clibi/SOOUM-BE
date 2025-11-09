package com.clip.infra.slack

import com.slack.api.Slack
import com.slack.api.model.block.Blocks.*
import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.block.composition.BlockCompositions.markdownText
import com.slack.api.model.block.composition.BlockCompositions.plainText
import com.slack.api.webhook.WebhookPayloads
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SlackService(
    @Value("\${slack.webhook.url}")
    private val webhookUrl: String
) {
    private val slack = Slack.getInstance()
    private val logger = LoggerFactory.getLogger(SlackService::class.java)

    fun sendSlackMessage(
        message: String?,
        exception: Exception?,
        requestInfo: RequestInfo?,
        userId: Long?,
        customFields: Map<String, Any?>
    ) {

        val blocks = buildBlocks(message, exception, requestInfo, userId, customFields)


        val displayText = when {
            exception != null -> "🚨 에러 발생: ${exception.javaClass.simpleName}"
            message != null -> message
            else -> "📢 알림"
        }

        try {
            slack.send(
                webhookUrl,
                WebhookPayloads.payload { payload ->
                    payload
                        .text(displayText)
                        .blocks(blocks)
                }
            )
            logger.info("Slack notification sent successfully")
        } catch (e: Exception) {
            logger.error("Failed to send Slack notification", e)
        }
    }

    private fun buildBlocks(
        message: String?,
        exception: Exception?,
        requestInfo: RequestInfo?,
        userId: Long?,
        customFields: Map<String, Any?>
    ): List<LayoutBlock> {
        val blocks = mutableListOf<LayoutBlock>()

        // 헤더
        val headerText = when {
            exception != null -> "🚨 에러 발생: ${exception.javaClass.simpleName}"
            message != null -> message
            else -> "📢 알림"
        }

        blocks.add(
            header { h ->
                h.text(
                    plainText { pt ->
                        pt.text(headerText)
                            .emoji(true)
                    }
                )
            }
        )

        blocks.add(divider())

        // 에러 정보 (에러인 경우)
        exception?.let { ex ->
            blocks.add(
                section { s ->
                    s.fields(
                        listOf(
                            markdownText("*에러 메시지:*\n${ex.message ?: "N/A"}"),
                            markdownText("*발생 시간:*\n${LocalDateTime.now()}")
                        )
                    )
                }
            )
        }

        // 일반 메시지 (에러가 아닌 경우)
        if (exception == null && message != null) {
            blocks.add(
                section { s ->
                    s.text(markdownText("*발생 시간:*\n${LocalDateTime.now()}"))
                }
            )
        }

        // 요청 정보 (있는 경우)
        requestInfo?.let { info ->
            blocks.add(
                section { s ->
                    s.fields(
                        listOf(
                            markdownText("*요청 URL:*\n`${info.url}`"),
                            markdownText("*HTTP Method:*\n`${info.method}`")
                        )
                    )
                }
            )

            if (info.pathVariables.isNotEmpty()) {
                val pathVarsText = info.pathVariables.entries
                    .joinToString("\n") { "${it.key}: ${it.value}" }
                blocks.add(
                    section { s ->
                        s.text(markdownText("*Path Variables:*\n```$pathVarsText```"))
                    }
                )
            }

            if (info.params.isNotEmpty()) {
                val paramsText = info.params.entries
                    .joinToString("\n") { "${it.key}: ${it.value}" }
                blocks.add(
                    section { s ->
                        s.text(markdownText("*요청 파라미터:*\n```$paramsText```"))
                    }
                )
            }

            info.body?.let { body ->
                val truncatedBody = if (body.length > 500) {
                    body.take(500) + "\n... (truncated)"
                } else body

                blocks.add(
                    section { s ->
                        s.text(markdownText("*요청 본문:*\n```$truncatedBody```"))
                    }
                )
            }

            info.remoteAddr.let { addr ->
                blocks.add(
                    section { s ->
                        s.text(markdownText("*클라이언트 IP:*\n```$addr```"))
                    }
                )
            }
        }

        // 사용자 정보
        userId?.let {
            blocks.add(
                section { s ->
                    s.text(markdownText("*User ID:* `$it`"))  // 짧은 값은 백틱 1개
                }
            )
        }

        // 커스텀 필드
        if (customFields.isNotEmpty()) {
            val customText = customFields.entries
                .joinToString("\n") { "${it.key}: ${it.value}" }
            blocks.add(
                section { s ->
                    s.text(markdownText("*추가 정보:*\n```$customText```"))
                }
            )
        }

        // 스택 트레이스 (에러인 경우)
        exception?.let { ex ->
            blocks.add(divider())

            val stackTrace = ex.stackTraceToString()
                .lines()
                .take(10)
                .joinToString("\n")
                .take(2900)  // Slack 제한 고려

            blocks.add(
                section { s ->
                    s.text(markdownText("*Stack Trace:*\n```$stackTrace```"))
                }
            )
        }

        return blocks
    }
}