package yjh.ontongsal.rewardservice.nats

import org.springframework.beans.factory.getBeansOfType
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class MessageHandlerFinder(
    private val applicationContext: ApplicationContext,
) {
    fun findAll(): List<MessageHandlerInfo> {
        val messageHandlers = applicationContext.getBeansOfType<NatsMessageHandler>().values
        return messageHandlers.map {
            MessageHandlerInfo(
                messageHandler = it,
                streamName = it.streamName,
                durableName = it.durableName,
                subjectName = it.subjectName,
            )
        }
    }
}