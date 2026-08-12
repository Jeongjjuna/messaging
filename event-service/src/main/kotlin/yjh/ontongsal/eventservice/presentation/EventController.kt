package yjh.ontongsal.eventservice.presentation

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yjh.ontongsal.eventservice.application.AttendanceEventService

@RestController
@RequestMapping("/api/v1/event")
class EventController(
    private val attendanceEventService: AttendanceEventService,
) {
    @PostMapping
    fun completeDailyAttendance() {
        attendanceEventService.completeDailyAttendance(
            eventId = "999",
            userId = "888",
        )
    }
}