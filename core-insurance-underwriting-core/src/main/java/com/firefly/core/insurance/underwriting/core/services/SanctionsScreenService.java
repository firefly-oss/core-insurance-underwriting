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


package com.firefly.core.insurance.underwriting.core.services;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.insurance.underwriting.interfaces.dtos.SanctionsScreenDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for recorded sanctions screening result. The check itself stays with the provider..
 */
public interface SanctionsScreenService {

    /**
     * Filters sanctionsScreens with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of SanctionsScreenDTO
     */
    Mono<PaginationResponse<SanctionsScreenDTO>> filterSanctionsScreens(UUID submissionId, FilterRequest<SanctionsScreenDTO> filterRequest);

    /**
     * Creates a SanctionsScreen.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<SanctionsScreenDTO> createSanctionsScreen(UUID submissionId, SanctionsScreenDTO dto);

    /**
     * Updates a SanctionsScreen.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<SanctionsScreenDTO> updateSanctionsScreen(UUID submissionId, UUID sanctionsScreenId, SanctionsScreenDTO dto);

    /**
     * Deletes a SanctionsScreen.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteSanctionsScreen(UUID submissionId, UUID sanctionsScreenId);

    /**
     * Loads a SanctionsScreen by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<SanctionsScreenDTO> getSanctionsScreenById(UUID submissionId, UUID sanctionsScreenId);
}
