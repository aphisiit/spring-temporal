package com.aphisiit.spring_temporal.activity.leave.impl

import com.aphisiit.spring_temporal.activity.leave.RequestIdActivity
import com.aphisiit.spring_temporal.configure.TemporalTaskQueues
import com.aphisiit.spring_temporal.service.leave.RequestGeneratorService
import io.temporal.spring.boot.ActivityImpl
import org.springframework.stereotype.Component

@Component
@ActivityImpl(taskQueues = [TemporalTaskQueues.LEAVE])
class RequestIdActivityImpl(
    private val requestGenerator: RequestGeneratorService
): RequestIdActivity {

    override fun generateRequestId(): String {
        return requestGenerator.generateRequestId("LEV").latestValue!!
    }
}