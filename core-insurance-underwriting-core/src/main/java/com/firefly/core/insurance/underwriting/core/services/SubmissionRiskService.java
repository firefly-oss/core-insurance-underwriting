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
import com.firefly.core.insurance.underwriting.interfaces.dtos.SubmissionRiskDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for risk item on a submission.
 */
public interface SubmissionRiskService {

    /**
     * Filters submissionRisks with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of SubmissionRiskDTO
     */
    Mono<PaginationResponse<SubmissionRiskDTO>> filterSubmissionRisks(UUID submissionId, FilterRequest<SubmissionRiskDTO> filterRequest);

    /**
     * Creates a SubmissionRisk.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<SubmissionRiskDTO> createSubmissionRisk(UUID submissionId, SubmissionRiskDTO dto);

    /**
     * Updates a SubmissionRisk.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<SubmissionRiskDTO> updateSubmissionRisk(UUID submissionId, UUID submissionRiskId, SubmissionRiskDTO dto);

    /**
     * Deletes a SubmissionRisk.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteSubmissionRisk(UUID submissionId, UUID submissionRiskId);

    /**
     * Loads a SubmissionRisk by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<SubmissionRiskDTO> getSubmissionRiskById(UUID submissionId, UUID submissionRiskId);
}
