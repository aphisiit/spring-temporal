package com.aphisiit.spring_temporal.service.leave

import com.aphisiit.spring_temporal.entity.RequestGeneratorEntity
import com.aphisiit.spring_temporal.mapper.leave.RequestGeneratorMapper
import com.aphisiit.spring_temporal.model.response.leave.RequestGeneratorResponse
import com.aphisiit.spring_temporal.repository.RequestGeneratorRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Service
class RequestGeneratorService(
    private val requestGeneratorRepository: RequestGeneratorRepository,
    private val mapper: RequestGeneratorMapper,
) {
    private val log = LoggerFactory.getLogger(RequestGeneratorService::class.java)

    @Transactional(readOnly = true)
    fun findByPrefix(prefix: String): RequestGeneratorResponse {
        if (prefix == "ERR") {
           throw RuntimeException("RequestGenerator prefix can not be with 'ERR'")
        }
        return  requestGeneratorRepository.findByPrefix(prefix)?.let {
            mapper.toModel(it)
        } ?: mapper.toModel(null)
    }

    @Transactional
    fun generateRequestId(prefix: String): RequestGeneratorResponse {
        val entity = requestGeneratorRepository.findForUpdateByPrefix(prefix)
            ?: RequestGeneratorEntity(prefix = prefix)

        entity.sequence += 1
        entity.latestValue = "${prefix}-${entity.sequence.toString().padStart(5, '0')}"
        entity.updatedAt = OffsetDateTime.now()

        val saved = requestGeneratorRepository.save(entity)
        log.info("Generated request id: ${saved.latestValue}")

        return mapper.toModel(saved)
    }
}
