package com.aphisiit.spring_temporal.activity.leave

import com.aphisiit.spring_temporal.model.workflow.leave.LeaveApprovalCommand
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveRejectionCommand
import com.aphisiit.spring_temporal.model.workflow.leave.LeaveRequestInput
import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

@ActivityInterface
interface LeaveRequestActivity {

    @ActivityMethod
    fun validateSubmission(input: LeaveRequestInput)

    @ActivityMethod
    fun persistSubmitted(input: LeaveRequestInput, workflowId: String)

    @ActivityMethod
    fun managerApprove(command: LeaveApprovalCommand)

    @ActivityMethod
    fun hrApprove(command: LeaveApprovalCommand)

    @ActivityMethod
    fun rejectRequest(command: LeaveRejectionCommand)
}
