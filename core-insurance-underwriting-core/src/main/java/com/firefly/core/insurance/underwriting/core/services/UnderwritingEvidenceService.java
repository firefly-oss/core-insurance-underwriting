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
import com.firefly.core.insurance.underwriting.interfaces.dtos.UnderwritingEvidenceDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for evidence reference on a submission. The document bytes stay in document management..
 */
public interface UnderwritingEvidenceService {

    /**
     * Filters underwritingEvidenceItems with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of UnderwritingEvidenceDTO
     */
    Mono<PaginationResponse<UnderwritingEvidenceDTO>> filterUnderwritingEvidenceItems(UUID submissionId, FilterRequest<UnderwritingEvidenceDTO> filterRequest);

    /**
     * Creates a UnderwritingEvidence.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<UnderwritingEvidenceDTO> createUnderwritingEvidence(UUID submissionId, UnderwritingEvidenceDTO dto);

    /**
     * Updates a UnderwritingEvidence.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<UnderwritingEvidenceDTO> updateUnderwritingEvidence(UUID submissionId, UUID underwritingEvidenceId, UnderwritingEvidenceDTO dto);

    /**
     * Deletes a UnderwritingEvidence.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteUnderwritingEvidence(UUID submissionId, UUID underwritingEvidenceId);

    /**
     * Loads a UnderwritingEvidence by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<UnderwritingEvidenceDTO> getUnderwritingEvidenceById(UUID submissionId, UUID underwritingEvidenceId);
}
