package com.aphisiit.spring_temporal.model.response.leave

import java.time.OffsetDateTime

data class RequestGeneratorResponse (
    val prefix: String,
    var sequence: Long?,
    var latestValue: String?,
    val createdAt: OffsetDateTime?,
    var updatedAt: OffsetDateTime?
)