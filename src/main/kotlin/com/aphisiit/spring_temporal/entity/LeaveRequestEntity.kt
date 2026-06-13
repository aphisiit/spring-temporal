package com.aphisiit.spring_temporal.entity

import com.aphisiit.spring_temporal.model.leave.LeaveRequestStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.OffsetDateTime

@Entity
@Table(name = "leave_request")
data class LeaveRequestEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "request_id", nullable = false, unique = true, length = 100)
    val requestId: String,

    @Column(name = "workflow_id", nullable = false, unique = true, length = 150)
    val workflowId: String,

    @Column(name = "employee_id", nullable = false, length = 100)
    val employeeId: String,

    @Column(name = "leave_type", nullable = false, length = 50)
    val leaveType: String,

    @Column(name = "start_date", nullable = false)
    val startDate: LocalDate,

    @Column(name = "end_date", nullable = false)
    val endDate: LocalDate,

    @Column(name = "reason", columnDefinition = "TEXT")
    val reason: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    var status: LeaveRequestStatus = LeaveRequestStatus.SUBMITTED,

    @Column(name = "manager_approved_by", length = 100)
    var managerApprovedBy: String? = null,

    @Column(name = "manager_approved_at")
    var managerApprovedAt: OffsetDateTime? = null,

    @Column(name = "hr_approved_by", length = 100)
    var hrApprovedBy: String? = null,

    @Column(name = "hr_approved_at")
    var hrApprovedAt: OffsetDateTime? = null,

    @Column(name = "rejected_by", length = 100)
    var rejectedBy: String? = null,

    @Column(name = "rejected_reason", columnDefinition = "TEXT")
    var rejectedReason: String? = null,

    @Column(name = "rejected_at")
    var rejectedAt: OffsetDateTime? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now()
)
