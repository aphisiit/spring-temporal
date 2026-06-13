package com.aphisiit.spring_temporal.workflow

import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface GreetingWorkflow {
    @WorkflowMethod
    fun greeting(name: String): String
}