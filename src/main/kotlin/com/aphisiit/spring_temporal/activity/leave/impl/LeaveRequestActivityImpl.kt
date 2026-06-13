package com.aphisiit.spring_temporal.activity.leave.impl

import com.aphisiit.spring_temporal.activity.leave.LeaveRequestActivity
import com.aphisiit.spring_temporal.configure.TemporalTaskQueues
import com.aphisiit.spring_temporal.model.event.LeaveRequestEvent
import com.aphisiit.spring_temporal.model.leave.LeaveEventType
import com.aphisiit.spring_temporal.model.leave.LeaveRequestStatus
import com.aphisiit.spring_temporal.model.request.leave.LeaveRequestCreateRequest
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveApprovalCommand
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveRejectionCommand
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveRequestInput
import com.aphisiit.spring_temporal.service.leave.LeaveRequestEventPublisher
import com.aphisiit.spring_temporal.service.leave.LeaveRequestService
import io.temporal.spring.boot.ActivityImpl
import org.springframework.stereotype.Component

@Component
@ActivityImpl(taskQueues = [TemporalTaskQueues.LEAVE])
class LeaveRequestActivityImpl(
    private val leaveRequestService: LeaveRequestService,
    private val eventPublisher: LeaveRequestEventPublisher
) : LeaveRequestActivity {

    override fun validateSubmission(input: LeaveRequestInput) {
        require(input.employeeId.isNotBlank()) { "employeeId is required" }
        require(input.leaveType.isNotBlank()) { "leaveType is required" }
        require(!input.endDate.isBefore(input.startDate)) { "endDate must not be before startDate" }
    }

    override fun persistSubmitted(input: LeaveRequestInput, workflowId: String) {
        leaveRequestService.createSubmitted(
            requestId = input.requestId,
            workflowId = workflowId,
            payload = LeaveRequestCreateRequest(
                employeeId = input.employeeId,
                leaveType = input.leaveType,
                startDate = input.startDate,
                endDate = input.endDate,
                reason = input.reason
            )
        )

        eventPublisher.publish(
            LeaveRequestEvent(
                requestId = input.requestId,
                employeeId = input.employeeId,
                eventType = LeaveEventType.SUBMITTED,
                status = LeaveRequestStatus.SUBMITTED,
                actorId = input.employeeId,
                reason = input.reason
            )
        )
    }

    override fun managerApprove(command: LeaveApprovalCommand) {
        val entity = leaveRequestService.markManagerApproved(command.requestId, command.approverId)
        eventPublisher.publish(
            LeaveRequestEvent(
                requestId = command.requestId,
                employeeId = entity.employeeId,
                eventType = LeaveEventType.MANAGER_APPROVED,
                status = entity.status,
                actorId = command.approverId,
                reason = null
            )
        )
    }

    override fun hrApprove(command: LeaveApprovalCommand) {
        val entity = leaveRequestService.markHrApproved(command.requestId, command.approverId)
        eventPublisher.publish(
            LeaveRequestEvent(
                requestId = command.requestId,
                employeeId = entity.employeeId,
                eventType = LeaveEventType.HR_APPROVED,
                status = entity.status,
                actorId = command.approverId,
                reason = null
            )
        )
    }

    override fun rejectRequest(command: LeaveRejectionCommand) {
        val entity = leaveRequestService.markRejected(
            requestId = command.requestId,
            rejectedBy = command.rejectedBy,
            reason = command.reason
        )

        eventPublisher.publish(
            LeaveRequestEvent(
                requestId = command.requestId,
                employeeId = entity.employeeId,
                eventType = LeaveEventType.REJECTED,
                status = entity.status,
                actorId = command.rejectedBy,
                reason = command.reason
            )
        )
    }
}
