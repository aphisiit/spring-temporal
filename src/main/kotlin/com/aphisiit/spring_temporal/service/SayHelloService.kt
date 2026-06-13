package com.aphisiit.spring_temporal.service

import com.aphisiit.spring_temporal.workflow.GreetingWorkflow
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SayHelloService(
    private val client: WorkflowClient
) {
    private val logger = LoggerFactory.getLogger(SayHelloService::class.java)

    fun sayHello(name: String): String {
        logger.info("Hello $name")
        val uuid = UUID.randomUUID().toString()
        val greetingWorkflow = client.newWorkflowStub(
            GreetingWorkflow::class.java,
            WorkflowOptions.newBuilder()
                .setTaskQueue("greetActivities-q")
                .setWorkflowId("greeting-$uuid")
                .build()
        )

        return greetingWorkflow.greeting(name)
    }
}