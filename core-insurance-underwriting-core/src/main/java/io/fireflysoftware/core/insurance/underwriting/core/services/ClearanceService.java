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


package io.fireflysoftware.core.insurance.underwriting.core.services;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.ClearanceDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for clearance of a submission against in-force business and the broker of record.
 */
public interface ClearanceService {

    /**
     * Filters clearances with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of ClearanceDTO
     */
    Mono<PaginationResponse<ClearanceDTO>> filterClearances(UUID submissionId, FilterRequest<ClearanceDTO> filterRequest);

    /**
     * Creates a Clearance.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<ClearanceDTO> createClearance(UUID submissionId, ClearanceDTO dto);

    /**
     * Updates a Clearance.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<ClearanceDTO> updateClearance(UUID submissionId, UUID clearanceId, ClearanceDTO dto);

    /**
     * Deletes a Clearance.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteClearance(UUID submissionId, UUID clearanceId);

    /**
     * Loads a Clearance by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<ClearanceDTO> getClearanceById(UUID submissionId, UUID clearanceId);
}
