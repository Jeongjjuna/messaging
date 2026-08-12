package yjh.ontongsal.rewardservice.nats

import io.nats.client.MessageHandler

data class MessageHandlerInfo(
    val messageHandler: MessageHandler,
    val streamName: String,
    val durableName: String,
    val subjectName: String,
)
