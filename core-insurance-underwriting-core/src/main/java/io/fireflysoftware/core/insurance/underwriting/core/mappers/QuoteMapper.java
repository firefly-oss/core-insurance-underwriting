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


package io.fireflysoftware.core.insurance.underwriting.core.mappers;

import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.QuoteDTO;
import io.fireflysoftware.core.insurance.underwriting.models.entities.Quote;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for Quote.
 */
@Mapper(componentModel = "spring")
public interface QuoteMapper {

    /**
     * Converts the entity to its DTO.
     *
     * @param entity persisted Quote
     * @return the DTO
     */
    QuoteDTO toDTO(Quote entity);

    /**
     * Converts the DTO to a new entity.
     *
     * @param dto inbound QuoteDTO
     * @return the entity
     */
    Quote toEntity(QuoteDTO dto);

    /**
     * Copies non-null DTO values onto an existing entity.
     * The identifier, parent link, and creation timestamp stay with the stored row.
     *
     * @param dto inbound values
     * @param entity stored entity
     */
    @Mapping(target = "quoteId", ignore = true)
    @Mapping(target = "submissionId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(QuoteDTO dto, @MappingTarget Quote entity);
}
