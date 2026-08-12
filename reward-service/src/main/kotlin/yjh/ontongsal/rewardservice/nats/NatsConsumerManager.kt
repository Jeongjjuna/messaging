package yjh.ontongsal.rewardservice.nats

import io.github.oshai.kotlinlogging.KotlinLogging
import io.nats.client.ConsumeOptions
import io.nats.client.JetStream
import io.nats.client.MessageConsumer
import io.nats.client.MessageHandler
import io.nats.client.api.AckPolicy
import io.nats.client.api.ConsumerConfiguration
import org.springframework.context.SmartLifecycle
import org.springframework.stereotype.Component
import java.time.Duration


private val log = KotlinLogging.logger {}

@Component
class NatsConsumerManager(
    private val jetStream: JetStream,
    private val messageHandlerFinder: MessageHandlerFinder,
) : SmartLifecycle {

    @Volatile
    private var running = false
    private val messageConsumers = mutableListOf<MessageConsumer>()

    override fun start() {
        try {
            val consumerCandidates: List<MessageHandlerInfo> = messageHandlerFinder.findAll()
            consumerCandidates.forEach {
                val messageConsumer = createConsumer(it.messageHandler, it.streamName, it.durableName, it.subjectName)
                messageConsumers.add(messageConsumer)
            }

            running = true

            log.info { "NATS Pull Consumer started. total consumers: ${messageConsumers.size}" }
        } catch (e: Exception) {
            log.error(e) { "Failed to start NATS Pull Consumer." }
            throw e // 여기서는 nats consumer 세팅이 올바르지 않으면 throw 를 전파하여 springboot 구동을 실패시킨다.(상황에 따라 선택하기)
        }
    }

    private fun createConsumer(messageHandler: MessageHandler, streamName: String, durableName: String, subjectName: String): MessageConsumer {
        // 1. 서버에 사전 생성되어 있는 Stream Context 가져오기
        val streamContext = jetStream.getStreamContext(streamName) // stream namm

        // 2. Consumer 형상(Configuration) 정의 (비즈니스 요구사항에 맞게 설정)
        val consumerConfig = ConsumerConfiguration.builder()
            .durable(durableName)
            .filterSubject(subjectName)
            .ackPolicy(AckPolicy.Explicit)
            .ackWait(Duration.ofSeconds(30))
            .maxDeliver(5)
            .build()

        // 3. [하이브리드 핵심] 서버에 Consumer가 없으면 신규 생성, 있으면 최신 설정으로 업데이트
        val consumerContext = streamContext.createOrUpdateConsumer(consumerConfig)

        // 4. Continuous Pull 세부 옵션 설정
        val consumeOptions = ConsumeOptions.builder()
            .batchSize(10) // 백그라운드 Pull 배치 크기
            .thresholdPercent(50) // 50% 처리 시 다음 배치 자동 Fetch
            .expiresIn(Duration.ofSeconds(2).toMillis()) // Pull 타임아웃
            .build()

        // 5. 지속적 메시지 수신 시작
        return consumerContext.consume(consumeOptions) {
            messageHandler.onMessage(it)
        }
    }

    override fun stop() {
        stop {}
    }

    override fun stop(callback: Runnable) {
        try {
            log.info { "Stopping NATS Pull Consumers..." }
            closeConsumers()
            running = false
            log.info { "NATS Pull Consumers stopped." }
        } finally {
            callback.run() // spring 에서 종료됐음을 알려주기.
        }
    }

    private fun closeConsumers() {
        messageConsumers.forEach { consumer ->
            try {
                consumer.close()
            } catch (e: Exception) {
                log.error(e) { "Failed to close NATS MessageConsumer." }
            }
        }
        messageConsumers.clear()
    }

    override fun isRunning(): Boolean {
        return running
    }

    override fun getPhase(): Int {
        return 0
    }
}