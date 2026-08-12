package yjh.ontongsal.rewardservice.presentation

import io.nats.client.Message
import org.springframework.stereotype.Component
import yjh.ontongsal.rewardservice.nats.NatsMessageHandler

@Component
class EventAlarmHandler: NatsMessageHandler {

    override val streamName: String
        get() = "EVENTS"
    override val durableName: String
        get() = "ALARM_EVENT_CONSUMER"
    override val subjectName: String
        get() = "event.completed"

    override fun handle(msg: Message) {
        val data = String(msg.data, Charsets.UTF_8)
        println(data)
        println("Alarm Event")
    }
}