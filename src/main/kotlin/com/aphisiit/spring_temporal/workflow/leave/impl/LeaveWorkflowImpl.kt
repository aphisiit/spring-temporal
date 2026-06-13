package com.aphisiit.spring_temporal.workflow.leave.impl

import com.aphisiit.spring_temporal.configure.TemporalTaskQueues
import com.aphisiit.spring_temporal.model.leave.ApprovalRole
import com.aphisiit.spring_temporal.model.leave.LeaveRequestStatus
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveApprovalCommand
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveRejectionCommand
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveRequestInput
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveWorkflowState
import com.aphisiit.spring_temporal.workflow.leave.LeaveWorkflow
import io.temporal.activity.ActivityOptions
import io.temporal.common.RetryOptions
import io.temporal.spring.boot.WorkflowImpl
import io.temporal.workflow.Workflow
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@WorkflowImpl(taskQueues = [TemporalTaskQueues.LEAVE])
class LeaveWorkflowImpl : LeaveWorkflow {

    private val retryOptions = RetryOptions.newBuilder()
        .setInitialInterval(1.seconds.toJavaDuration())
        .setMaximumInterval(5.seconds.toJavaDuration())
        .setBackoffCoefficient(2.0)
        .setMaximumAttempts(3)
        .build()

    private val defaultActivityOptions = ActivityOptions.newBuilder()
        .setRetryOptions(retryOptions)
        .setStartToCloseTimeout(5.seconds.toJavaDuration())
        .setScheduleToCloseTimeout(50.seconds.toJavaDuration())
        .build()

    private var requestId: String = ""
    private var status: LeaveRequestStatus = LeaveRequestStatus.SUBMITTED
    private var managerApproval: LeaveApprovalCommand? = null
    private var hrApproval: LeaveApprovalCommand? = null
    private var rejection: LeaveRejectionCommand? = null

    override fun submit(input: LeaveRequestInput) {
        requestId = input.requestId
        status = LeaveRequestStatus.SUBMITTED

        Workflow.await {
            rejection != null || managerApproval != null
        }

        rejection?.let {
            status = LeaveRequestStatus.REJECTED
            return
        }

        managerApproval?.let {
            if (it.role == ApprovalRole.MANAGER) {
                status = LeaveRequestStatus.MANAGER_APPROVED
                managerApproval = null
            }
        }

        Workflow.await {
            rejection != null || hrApproval != null
        }

        rejection?.let {
            status = LeaveRequestStatus.REJECTED
            return
        }

        hrApproval?.let {
            if (it.role == ApprovalRole.HR) {
                status = LeaveRequestStatus.HR_APPROVED
                hrApproval = null
            }
        }
    }

    override fun approve(command: LeaveApprovalCommand) {
        when (command.role) {
            ApprovalRole.MANAGER -> managerApproval = command
            ApprovalRole.HR -> hrApproval = command
        }
    }

    override fun reject(command: LeaveRejectionCommand) {
        rejection = command
    }

    override fun currentState(): LeaveWorkflowState {
        return LeaveWorkflowState(
            requestId = requestId,
            status = status
        )
    }
}