package com.aphisiit.spring_temporal.service.leave

import com.aphisiit.spring_temporal.entity.LeaveRequestEntity
import com.aphisiit.spring_temporal.mapper.leave.LeaveRequestMapper
import com.aphisiit.spring_temporal.model.leave.LeaveRequestStatus
import com.aphisiit.spring_temporal.model.request.leave.LeaveRequestCreateRequest
import com.aphisiit.spring_temporal.model.response.leave.LeaveRequestResponse
import com.aphisiit.spring_temporal.repository.LeaveRequestRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Service
class LeaveRequestService(
    private val leaveRequestRepository: LeaveRequestRepository,
    private val mapper: LeaveRequestMapper
) {

    @Transactional
    fun createSubmitted(requestId: String, workflowId: String, payload: LeaveRequestCreateRequest): LeaveRequestEntity {
        val entity = LeaveRequestEntity(
            requestId = requestId,
            workflowId = workflowId,
            employeeId = payload.employeeId,
            leaveType = payload.leaveType,
            startDate = payload.startDate,
            endDate = payload.endDate,
            reason = payload.reason,
            status = LeaveRequestStatus.SUBMITTED
        )
        return leaveRequestRepository.save(entity)
    }

    @Transactional
    fun markManagerApproved(requestId: String, approverId: String): LeaveRequestEntity {
        val entity = getEntityByRequestId(requestId)
        if (entity.status != LeaveRequestStatus.SUBMITTED) {
            throw IllegalArgumentException("Only SUBMITTED request can be manager-approved")
        }

        entity.status = LeaveRequestStatus.MANAGER_APPROVED
        entity.managerApprovedBy = approverId
        entity.managerApprovedAt = OffsetDateTime.now()
        entity.updatedAt = OffsetDateTime.now()
        return leaveRequestRepository.save(entity)
    }

    @Transactional
    fun markHrApproved(requestId: String, approverId: String): LeaveRequestEntity {
        val entity = getEntityByRequestId(requestId)
        if (entity.status != LeaveRequestStatus.MANAGER_APPROVED) {
            throw IllegalArgumentException("Only MANAGER_APPROVED request can be HR-approved")
        }

        entity.status = LeaveRequestStatus.HR_APPROVED
        entity.hrApprovedBy = approverId
        entity.hrApprovedAt = OffsetDateTime.now()
        entity.updatedAt = OffsetDateTime.now()
        return leaveRequestRepository.save(entity)
    }

    @Transactional
    fun markRejected(requestId: String, rejectedBy: String, reason: String): LeaveRequestEntity {
        val entity = getEntityByRequestId(requestId)
        if (entity.status == LeaveRequestStatus.HR_APPROVED || entity.status == LeaveRequestStatus.REJECTED) {
            throw IllegalArgumentException("Request is already finalized")
        }

        entity.status = LeaveRequestStatus.REJECTED
        entity.rejectedBy = rejectedBy
        entity.rejectedReason = reason
        entity.rejectedAt = OffsetDateTime.now()
        entity.updatedAt = OffsetDateTime.now()
        return leaveRequestRepository.save(entity)
    }

    @Transactional(readOnly = true)
    fun getByRequestId(requestId: String): LeaveRequestResponse {
        return mapper.toResponse(getEntityByRequestId(requestId))
    }

    @Transactional(readOnly = true)
    fun getEntityByRequestId(requestId: String): LeaveRequestEntity {
        return leaveRequestRepository.findByRequestId(requestId)
            ?: throw IllegalArgumentException("Leave request not found for requestId: $requestId")
    }
}
