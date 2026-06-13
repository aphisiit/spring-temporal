package com.aphisiit.spring_temporal.service.leave

import com.aphisiit.spring_temporal.configure.TemporalTaskQueues
import com.aphisiit.spring_temporal.model.event.LeaveRequestEvent
import com.aphisiit.spring_temporal.model.leave.ApprovalRole
import com.aphisiit.spring_temporal.model.leave.LeaveEventType
import com.aphisiit.spring_temporal.model.leave.LeaveRequestStatus
import com.aphisiit.spring_temporal.model.request.leave.LeaveRequestApproveRequest
import com.aphisiit.spring_temporal.model.request.leave.LeaveRequestCreateRequest
import com.aphisiit.spring_temporal.model.request.leave.LeaveRequestRejectRequest
import com.aphisiit.spring_temporal.model.response.leave.LeaveCommandAcceptedResponse
import com.aphisiit.spring_temporal.model.response.leave.LeaveRequestAcceptedResponse
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveApprovalCommand
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveRejectionCommand
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveRequestInput
import com.aphisiit.spring_temporal.workflow.leave.LeaveWorkflow
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LeaveWorkflowStarterService(
    private val workflowClient: WorkflowClient,
    private val leaveRequestService: LeaveRequestService,
    private val requestGeneratorService: RequestGeneratorService,
    private val eventPublisher: LeaveRequestEventPublisher
) {

    fun submit(request: LeaveRequestCreateRequest): LeaveRequestAcceptedResponse {
        val requestId = requestGeneratorService.generateRequestId("LEV").latestValue
            ?: throw IllegalStateException("Failed to generate leave request id")

        val workflowId = requestId

        val workflow = workflowClient.newWorkflowStub(
            LeaveWorkflow::class.java,
            WorkflowOptions.newBuilder()
                .setTaskQueue(TemporalTaskQueues.LEAVE)
                .setWorkflowId(workflowId)
                .build()
        )

        leaveRequestService.createSubmitted(
            requestId = requestId,
            workflowId = workflowId,
            payload = request
        )

        eventPublisher.publish(
            LeaveRequestEvent(
                requestId = requestId,
                employeeId = request.employeeId,
                eventType = LeaveEventType.SUBMITTED,
                status = LeaveRequestStatus.SUBMITTED,
                actorId = request.employeeId,
                reason = request.reason
            )
        )

        WorkflowClient.start(
            workflow::submit,
            LeaveRequestInput(
                requestId = requestId,
                employeeId = request.employeeId,
                leaveType = request.leaveType,
                startDate = request.startDate,
                endDate = request.endDate,
                reason = request.reason
            )
        )

        return LeaveRequestAcceptedResponse(
            requestId = requestId,
            workflowId = workflowId,
            status = LeaveRequestStatus.SUBMITTED
        )
    }

    fun approve(requestId: String, request: LeaveRequestApproveRequest): LeaveCommandAcceptedResponse {
        val entity = leaveRequestService.getEntityByRequestId(requestId)

        val updated = when (request.role) {
            ApprovalRole.MANAGER -> {
                require(entity.status == LeaveRequestStatus.SUBMITTED) {
                    "Manager can approve only SUBMITTED requests"
                }
                leaveRequestService.markManagerApproved(requestId, request.approverId)
            }
            ApprovalRole.HR -> {
                require(entity.status == LeaveRequestStatus.MANAGER_APPROVED) {
                    "HR can approve only MANAGER_APPROVED requests"
                }
                leaveRequestService.markHrApproved(requestId, request.approverId)
            }
        }

        eventPublisher.publish(
            LeaveRequestEvent(
                requestId = updated.requestId,
                employeeId = updated.employeeId,
                eventType = when (request.role) {
                    ApprovalRole.MANAGER -> LeaveEventType.MANAGER_APPROVED
                    ApprovalRole.HR -> LeaveEventType.HR_APPROVED
                },
                status = updated.status,
                actorId = request.approverId,
                reason = null
            )
        )

        val workflow = workflowClient.newWorkflowStub(LeaveWorkflow::class.java, entity.workflowId)
        workflow.approve(
            LeaveApprovalCommand(
                requestId = requestId,
                approverId = request.approverId,
                role = request.role
            )
        )

        return LeaveCommandAcceptedResponse(
            requestId = entity.requestId,
            workflowId = entity.workflowId,
            status = entity.status,
            command = "APPROVE_SIGNAL_ACCEPTED"
        )
    }

    fun reject(requestId: String, request: LeaveRequestRejectRequest): LeaveCommandAcceptedResponse {
        val entity = leaveRequestService.getEntityByRequestId(requestId)
        require(entity.status != LeaveRequestStatus.HR_APPROVED && entity.status != LeaveRequestStatus.REJECTED) {
            "Request is already finalized"
        }

        val updated = leaveRequestService.markRejected(
            requestId = requestId,
            rejectedBy = request.rejectedBy,
            reason = request.reason
        )

        eventPublisher.publish(
            LeaveRequestEvent(
                requestId = updated.requestId,
                employeeId = updated.employeeId,
                eventType = LeaveEventType.REJECTED,
                status = updated.status,
                actorId = request.rejectedBy,
                reason = request.reason
            )
        )

        val workflow = workflowClient.newWorkflowStub(LeaveWorkflow::class.java, entity.workflowId)
        workflow.reject(
            LeaveRejectionCommand(
                requestId = requestId,
                rejectedBy = request.rejectedBy,
                reason = request.reason,
                role = request.role
            )
        )

        return LeaveCommandAcceptedResponse(
            requestId = entity.requestId,
            workflowId = entity.workflowId,
            status = entity.status,
            command = "REJECT_SIGNAL_ACCEPTED"
        )
    }
}
