package yjh.ontongsal.rewardservice.presentation

import io.nats.client.Message
import org.springframework.stereotype.Component
import yjh.ontongsal.rewardservice.nats.NatsMessageHandler


/**
 * NATS 메시지를 처리하려면 NatsMessageHandler를 구현해주세요.
 *
 * 아래 3가지 프로퍼티는 어떤 메시지를 소비할지 결정하기 위한 정보입니다.
 * - streamName: 메시지가 저장된 NATS Stream 이름
 *   - NATS Server에 해당 Stream이 미리 생성되어 있어야 합니다.
 *   - Stream이 존재하지 않으면 Spring Boot 애플리케이션이 시작되지 않습니다.
 * - durableName: NATS Consumer를 식별하는 고유한 이름
 * - subjectName: 소비할 메시지의 Subject
 *
 * 실제 메시지를 처리하는 비즈니스 로직은 handle()에 구현해주세요.
 *
 * 메시지 ACK와 예외 처리는 NatsMessageHandler에서 공통으로 처리합니다.
 * 따라서 handle()에서는 별도로 try-catch를 사용하거나 ack()를 호출하지 않아도 됩니다.
 * 비즈니스 로직에서 예외가 발생하면 ACK되지 않고, NATS가 메시지를 다시 전달합니다.
 */
@Component
class EventRewardHandler : NatsMessageHandler {

    override val streamName: String
        get() = "EVENTS"
    override val durableName: String
        get() = "REWARD_EVENT_CONSUMER"
    override val subjectName: String
        get() = "event.completed"

    override fun handle(msg: Message) {
        val data = String(msg.data, Charsets.UTF_8)
        println(data)
    }
}