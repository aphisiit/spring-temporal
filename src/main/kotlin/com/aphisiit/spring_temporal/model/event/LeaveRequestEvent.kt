package com.aphisiit.spring_temporal.model.event

import com.aphisiit.spring_temporal.model.leave.LeaveEventType
import com.aphisiit.spring_temporal.model.leave.LeaveRequestStatus
import java.time.OffsetDateTime

data class LeaveRequestEvent(
    val requestId: String,
    val employeeId: String,
    val eventType: LeaveEventType,
    val status: LeaveRequestStatus,
    val actorId: String?,
    val reason: String?,
    val eventAt: OffsetDateTime = OffsetDateTime.now()
)
