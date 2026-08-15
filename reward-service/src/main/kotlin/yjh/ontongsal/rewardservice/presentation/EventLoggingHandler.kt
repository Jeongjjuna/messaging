package yjh.ontongsal.rewardservice.presentation

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.annotation.BackOff
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
class EventLoggingHandler {

    @KafkaListener(
        topics = ["event.completed"],
        groupId = "event-logging-group-id",
        containerFactory = "kafkaListener"
    )
    @RetryableTopic( // 어노테이션 적용시 자동으로 DLT 토픽 생성
        attempts = "5", // 주로 3 ~ 5회
        backOff = BackOff(delay = 1000, multiplier = 2.0), // 지수 백오프 방식
        dltTopicSuffix = ".dlt" // default = topic 이름 + -dlt
    )
    fun handle(
        @Header(KafkaHeaders.RECEIVED_KEY) key: String?,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        @Header(KafkaHeaders.OFFSET) offset: Long,
        @Payload message: String
    ) {
        log.info { "Kafka message received topic=$topic partition=$partition offset=$offset key=$key payload=$message" }
    }
}