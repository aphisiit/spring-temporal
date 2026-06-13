package com.aphisiit.spring_temporal.repository

import com.aphisiit.spring_temporal.entity.RequestGeneratorEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository

@Repository
interface RequestGeneratorRepository : JpaRepository<RequestGeneratorEntity, Long> {
    fun findByPrefix(prefix: String): RequestGeneratorEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateByPrefix(prefix: String): RequestGeneratorEntity?
}