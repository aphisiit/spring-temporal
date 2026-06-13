package com.aphisiit.spring_temporal.activity.impl

import com.aphisiit.spring_temporal.activity.GreetActivities
import io.temporal.spring.boot.ActivityImpl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@ActivityImpl(taskQueues = ["greetActivities-q"])
class GreetActivitiesImpl: GreetActivities {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun greet(name: String): String {
        if(name.lowercase() == "hi" ) {
            throw RuntimeException("Can't greet with name 'hi'!")
        }
        log.info("Greeting $name")
        return "Hello, $name!"
    }

    override fun goodBye(name: String): String {
        log.info("It's time to good by $name")
        return "Goodbye $name!"
    }
}