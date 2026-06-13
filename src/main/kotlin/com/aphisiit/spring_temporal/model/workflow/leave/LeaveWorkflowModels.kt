package com.aphisiit.spring_temporal.model.workflow.leave

import com.aphisiit.spring_temporal.model.leave.ApprovalRole
import com.aphisiit.spring_temporal.model.leave.LeaveRequestStatus
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class LeaveRequestInput @JsonCreator constructor(
    @JsonProperty("requestId") val requestId: String,
    @JsonProperty("employeeId") val employeeId: String,
    @JsonProperty("leaveType") val leaveType: String,
    @JsonProperty("startDate") val startDate: LocalDate,
    @JsonProperty("endDate") val endDate: LocalDate,
    @JsonProperty("reason") val reason: String?
)

data class LeaveApprovalCommand @JsonCreator constructor(
    @JsonProperty("requestId") val requestId: String,
    @JsonProperty("approverId") val approverId: String,
    @JsonProperty("role") val role: ApprovalRole
)

data class LeaveRejectionCommand @JsonCreator constructor(
    @JsonProperty("requestId") val requestId: String,
    @JsonProperty("rejectedBy") val rejectedBy: String,
    @JsonProperty("reason") val reason: String,
    @JsonProperty("role") val role: ApprovalRole
)

data class LeaveWorkflowState @JsonCreator constructor(
    @JsonProperty("requestId") val requestId: String,
    @JsonProperty("status") val status: LeaveRequestStatus
)
