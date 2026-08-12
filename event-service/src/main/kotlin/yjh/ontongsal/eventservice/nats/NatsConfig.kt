package yjh.ontongsal.eventservice.nats

import io.nats.client.Connection
import io.nats.client.JetStream
import io.nats.client.Nats
import io.nats.client.Options
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class NatsConfig {

    @Bean
    fun natsPublishConnection(): Connection {
        val options = Options.Builder()
            .servers(
                arrayOf(
                    "nats://localhost:4222",           // 우리가 띄운 nats
                    "nats://localhost:4223",           // 백업서버 -> 저 서버가 없으면 무시
                    "nats://localhost:4224"
                )
            )
            .maxReconnects(-1)                         // 무한 재연결
            .reconnectWait(Duration.ofSeconds(10))     // 재연결 시도하는 상황에서, 이 사이의 대기시간을 지정.
            .reconnectJitter(Duration.ofSeconds(1))    // 재시도 지터 : Thundering Herd
            .reconnectJitterTls(Duration.ofSeconds(1))
            .connectionTimeout(Duration.ofSeconds(5))  // 서버 연결요청시 대기시간(5초안에 응답없으면 실패)
            .pingInterval(Duration.ofSeconds(20))      // 서버가 클라이언트에게 PING 을 보재는 간격(TCP 는 연결이 끊겨도 서버가 알지 못함. 그래서 핑/퐁을 한번씩 함)
            .maxPingsOut(2)                            // 핑/퐁 2회 실패하면 연결 종료
            .connectionListener(NatsConnectionListener())
            .build()
        return Nats.connect(options)
    }

    @Bean
    fun natsPublishJetStream(
        connection: Connection
    ): JetStream {
        return connection.jetStream()
    }
}