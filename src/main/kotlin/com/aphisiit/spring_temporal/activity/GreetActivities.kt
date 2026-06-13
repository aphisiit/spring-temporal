package com.aphisiit.spring_temporal.activity

import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

@ActivityInterface
interface GreetActivities {
    @ActivityMethod
    fun greet(name: String): String
    @ActivityMethod
    fun goodBye(name: String): String
}