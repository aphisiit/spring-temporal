package com.aphisiit.spring_temporal.activity.leave

import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

@ActivityInterface
interface RequestIdActivity {
    @ActivityMethod
    fun generateRequestId(): String
}