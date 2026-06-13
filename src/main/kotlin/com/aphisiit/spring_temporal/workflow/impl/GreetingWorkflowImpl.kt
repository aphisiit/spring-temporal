package com.aphisiit.spring_temporal.workflow.impl

import com.aphisiit.spring_temporal.activity.GreetActivities
import com.aphisiit.spring_temporal.workflow.GreetingWorkflow
import io.temporal.activity.ActivityOptions
import io.temporal.common.RetryOptions
import io.temporal.spring.boot.WorkflowImpl
import io.temporal.workflow.Workflow
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@WorkflowImpl(taskQueues = ["greetActivities-q"])
class GreetingWorkflowImpl: GreetingWorkflow {

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

    private val activities: GreetActivities = Workflow.newActivityStub(
        GreetActivities::class.java,
        defaultActivityOptions
    )

    override fun greeting(name: String): String {
        activities.greet(name)
        return activities.goodBye(name)
    }
}