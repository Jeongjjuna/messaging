package yjh.ontongsal.rewardservice.nats

import io.github.oshai.kotlinlogging.KotlinLogging
import io.nats.client.Message
import io.nats.client.MessageHandler
import java.util.UUID

private val log = KotlinLogging.logger {}

interface NatsMessageHandler: MessageHandler {
    val streamName: String
    val subjectName: String
    val durableName: String

    override fun onMessage(msg: Message) {
        val traceId = msg.headers?.getFirst("traceId") ?: run {
            log.warn { "traceId is missing. Generate new traceId. subject=$subjectName" }
            UUID.randomUUID().toString()
            // MDC 에 traceID 추가.
        }

        try {
            log.info { "Message processing started. subject=$subjectName, traceId=$traceId" }
            handle(msg)
            msg.ack()
            log.info { "Message processing completed. subject=$subjectName, traceId=$traceId" }
        } catch (e: Exception) {
            log.error(e) { "Message processing failed. subject=$subjectName, traceId=$traceId" }
        }
    }

    fun handle(msg: Message)
}