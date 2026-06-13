package com.aphisiit.spring_temporal.workflow.leave

import com.aphisiit.spring_temporal.model.workflow.leave.LeaveApprovalCommand
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveRejectionCommand
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveRequestInput
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveWorkflowState
import io.temporal.workflow.QueryMethod
import io.temporal.workflow.SignalMethod
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface LeaveWorkflow {
    @WorkflowMethod
    fun submit(input: LeaveRequestInput)

    @SignalMethod
    fun approve(command: LeaveApprovalCommand)

    @SignalMethod
    fun reject(command: LeaveRejectionCommand)

    @QueryMethod
    fun currentState(): LeaveWorkflowState
}