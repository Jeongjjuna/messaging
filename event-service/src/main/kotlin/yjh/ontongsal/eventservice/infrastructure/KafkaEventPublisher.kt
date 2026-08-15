package yjh.ontongsal.eventservice.infrastructure

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper


@Component
class KafkaEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
) {

    /**
     * 내부적으로 비동기 호출: CompletableFuture
     * 최소 1번 발행이 중요할 경우, 스케쥴러를 통해서 발행처리되지 않은 데이터를 발행해줘야 한다.
     */
    fun publish(topic: String, payload: Any) {
        val value = objectMapper.writeValueAsString(payload)
        kafkaTemplate.send(topic, value);
    }

    /**
     * 내부적으로 비동기 호출: CompletableFuture
     * 최소 1번 발행이 중요할 경우, 스케쥴러를 통해서 발행처리되지 않은 데이터를 발행해줘야 한다.
     */
    fun publishWithKey(topic: String, key: String, payload: Any) {
        val value = objectMapper.writeValueAsString(payload)
        kafkaTemplate.send(topic, key, value);
    }
}