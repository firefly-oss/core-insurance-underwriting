/*
 * Copyright 2026 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.core.insurance.underwriting.core.mappers;

import com.firefly.core.insurance.underwriting.interfaces.dtos.ClearanceDTO;
import com.firefly.core.insurance.underwriting.models.entities.Clearance;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for Clearance.
 */
@Mapper(componentModel = "spring")
public interface ClearanceMapper {

    /**
     * Converts the entity to its DTO.
     *
     * @param entity persisted Clearance
     * @return the DTO
     */
    ClearanceDTO toDTO(Clearance entity);

    /**
     * Converts the DTO to a new entity.
     *
     * @param dto inbound ClearanceDTO
     * @return the entity
     */
    Clearance toEntity(ClearanceDTO dto);

    /**
     * Copies non-null DTO values onto an existing entity.
     * The identifier, parent link, and creation timestamp stay with the stored row.
     *
     * @param dto inbound values
     * @param entity stored entity
     */
    @Mapping(target = "clearanceId", ignore = true)
    @Mapping(target = "submissionId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ClearanceDTO dto, @MappingTarget Clearance entity);
}
