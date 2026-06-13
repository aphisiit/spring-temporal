package com.aphisiit.spring_temporal.model.response.leave

import com.aphisiit.spring_temporal.model.leave.LeaveRequestStatus
import java.time.LocalDate
import java.time.OffsetDateTime

data class LeaveRequestResponse(
    val requestId: String,
    val workflowId: String,
    val employeeId: String,
    val leaveType: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val reason: String?,
    val status: LeaveRequestStatus,
    val managerApprovedBy: String?,
    val managerApprovedAt: OffsetDateTime?,
    val hrApprovedBy: String?,
    val hrApprovedAt: OffsetDateTime?,
    val rejectedBy: String?,
    val rejectedReason: String?,
    val rejectedAt: OffsetDateTime?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

data class LeaveRequestAcceptedResponse(
    val requestId: String,
    val workflowId: String,
    val status: LeaveRequestStatus
)

data class LeaveCommandAcceptedResponse(
    val requestId: String,
    val workflowId: String,
    val status: LeaveRequestStatus,
    val command: String
)
