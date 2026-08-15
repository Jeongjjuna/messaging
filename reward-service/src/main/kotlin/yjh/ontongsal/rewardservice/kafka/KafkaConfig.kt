package yjh.ontongsal.rewardservice.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties


@Configuration
class KafkaConfig {

    private val kafkaServer = "localhost:9092"

    @Bean
    fun consumerFactory(): ConsumerFactory<String, Any> {
        val config: MutableMap<String, Any> = HashMap()
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaServer
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return DefaultKafkaConsumerFactory(config)
    }

    @Bean
    fun kafkaListener(
        consumerFactory: ConsumerFactory<String, Any>,
    ): ConcurrentKafkaListenerContainerFactory<String, String> {
        val listener = ConcurrentKafkaListenerContainerFactory<String, String>()
        listener.setConsumerFactory(consumerFactory)

        // 수동 커밋을 위한 설정
        listener.containerProperties.ackMode = ContainerProperties.AckMode.RECORD; // or BATCH : 멱등성 보장하도록 구현해야함.
        return listener
    }
}