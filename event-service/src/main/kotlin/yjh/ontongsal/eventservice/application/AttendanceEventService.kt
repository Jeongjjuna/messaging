package yjh.ontongsal.eventservice.application

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import yjh.ontongsal.eventservice.infrastructure.NatsEventPublisher
import java.util.UUID

private val log = KotlinLogging.logger {}

@Service
class AttendanceEventService(
    private val natsEventPublisher: NatsEventPublisher
) {
    fun completeDailyAttendance(eventId: String, userId: String) {
        // 1. 비즈니스 로직(transaction)
        log.info { "completeDailyAttendance eventId=$eventId userId=$userId" }
        val rewardId = UUID.randomUUID().toString()

        // 2. 이벤트 완료 메세지 발행
        natsEventPublisher.publish(
            subject = "event.completed",
            uniqueMessageId = "attendance-complete-$rewardId",
            payload = AttendanceCompletedEvent(
                eventId = eventId,
                eventType = "ATTENDANCE",
                userId = userId,
                rewardId = rewardId,
            )
        )
    }

    private data class AttendanceCompletedEvent(
        val eventId: String,
        val eventType: String,
        val userId: String,
        val rewardId: String,
    )
}