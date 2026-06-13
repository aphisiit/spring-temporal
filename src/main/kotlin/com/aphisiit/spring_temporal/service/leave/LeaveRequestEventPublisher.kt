package com.aphisiit.spring_temporal.service.leave

import com.aphisiit.spring_temporal.model.event.LeaveRequestEvent
import tools.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class LeaveRequestEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    @Value("\${app.kafka.leave-topic:leave.request.events}")
    private val leaveTopic: String
) {
    private val log = LoggerFactory.getLogger(LeaveRequestEventPublisher::class.java)

    fun publish(event: LeaveRequestEvent) {
        val payload = objectMapper.writeValueAsString(event)
        kafkaTemplate.send(leaveTopic, event.requestId, payload)
        log.info("Published leave event type={} requestId={}", event.eventType, event.requestId)
    }
}
