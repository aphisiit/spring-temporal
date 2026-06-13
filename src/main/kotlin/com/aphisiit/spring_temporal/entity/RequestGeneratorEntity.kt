package com.aphisiit.spring_temporal.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "request_generator")
data class RequestGeneratorEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "prefix", nullable = false, length = 50)
    val prefix: String,

    @Column(name = "sequence", nullable = false)
    var sequence: Long = 0,

    @Column(name = "latest_value", nullable = true, length = 100)
    var latestValue: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now()
)
