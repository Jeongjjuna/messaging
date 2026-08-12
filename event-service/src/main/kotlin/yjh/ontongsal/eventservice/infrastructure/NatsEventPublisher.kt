package yjh.ontongsal.eventservice.infrastructure

import io.github.oshai.kotlinlogging.KotlinLogging
import io.nats.client.JetStream
import io.nats.client.impl.Headers
import io.nats.client.impl.NatsMessage
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

private val log = KotlinLogging.logger {}

@Component
class NatsEventPublisher(
    private val jetStream: JetStream,
    private val objectMapper: ObjectMapper,
) {

    fun publish(
        subject: String,
        uniqueMessageId: String,
        payload: Any,
    ) {
        val headers = Headers().apply {
            add(MESSAGE_ID_HEADER, uniqueMessageId)
        }
        val message = NatsMessage.builder()
            .subject(subject)
            .headers(headers)
            .data(objectMapper.writeValueAsBytes(payload))
            .build()

        val ack = jetStream.publish(message)

        if (ack.isDuplicate) {
            log.warn { "[Nats] Duplicate Message. msgId=$uniqueMessageId" }
            return
        }
        log.info { "[Nats] Publish Success. stream=${ack.stream}, seq=${ack.seqno}" }
    }

    companion object {
        private const val MESSAGE_ID_HEADER = "Nats-Msg-Id"
    }
}