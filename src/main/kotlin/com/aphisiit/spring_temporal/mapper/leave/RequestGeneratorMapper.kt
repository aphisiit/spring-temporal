package com.aphisiit.spring_temporal.mapper.leave

import com.aphisiit.spring_temporal.entity.RequestGeneratorEntity
import com.aphisiit.spring_temporal.model.response.leave.RequestGeneratorResponse
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface RequestGeneratorMapper {
    fun toModel(entity: RequestGeneratorEntity?): RequestGeneratorResponse
}