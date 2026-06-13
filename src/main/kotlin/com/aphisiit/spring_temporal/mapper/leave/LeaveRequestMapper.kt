package com.aphisiit.spring_temporal.mapper.leave

import com.aphisiit.spring_temporal.entity.LeaveRequestEntity
import com.aphisiit.spring_temporal.model.response.leave.LeaveRequestResponse
import org.springframework.stereotype.Component

@Component
class LeaveRequestMapper {
    fun toResponse(entity: LeaveRequestEntity): LeaveRequestResponse {
        return LeaveRequestResponse(
            requestId = entity.requestId,
            workflowId = entity.workflowId,
            employeeId = entity.employeeId,
            leaveType = entity.leaveType,
            startDate = entity.startDate,
            endDate = entity.endDate,
            reason = entity.reason,
            status = entity.status,
            managerApprovedBy = entity.managerApprovedBy,
            managerApprovedAt = entity.managerApprovedAt,
            hrApprovedBy = entity.hrApprovedBy,
            hrApprovedAt = entity.hrApprovedAt,
            rejectedBy = entity.rejectedBy,
            rejectedReason = entity.rejectedReason,
            rejectedAt = entity.rejectedAt,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
}
