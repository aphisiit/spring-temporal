package com.aphisiit.spring_temporal.model.request.leave

import com.aphisiit.spring_temporal.model.leave.ApprovalRole
import java.time.LocalDate

data class LeaveRequestCreateRequest(
    val employeeId: String,
    val leaveType: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val reason: String?
)

data class LeaveRequestApproveRequest(
    val approverId: String,
    val role: ApprovalRole
)

data class LeaveRequestRejectRequest(
    val rejectedBy: String,
    val role: ApprovalRole,
    val reason: String
)
