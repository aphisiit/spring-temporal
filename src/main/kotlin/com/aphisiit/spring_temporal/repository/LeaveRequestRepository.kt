package com.aphisiit.spring_temporal.repository

import com.aphisiit.spring_temporal.entity.LeaveRequestEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LeaveRequestRepository : JpaRepository<LeaveRequestEntity, Long> {
    fun findByRequestId(requestId: String): LeaveRequestEntity?
}
