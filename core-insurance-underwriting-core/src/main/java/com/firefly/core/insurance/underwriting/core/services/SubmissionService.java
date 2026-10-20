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
import com.firefly.core.insurance.underwriting.interfaces.dtos.SubmissionDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for underwriting submission received for a product and applicant.
 */
public interface SubmissionService {

    /**
     * Filters submissions with the given criteria.
     *
     * @param filterRequest filter criteria
     * @return a page of SubmissionDTO
     */
    Mono<PaginationResponse<SubmissionDTO>> filterSubmissions(FilterRequest<SubmissionDTO> filterRequest);

    /**
     * Creates a Submission.
     *
     * @param dto values to store
     * @return the stored DTO
     */
    Mono<SubmissionDTO> createSubmission(SubmissionDTO dto);

    /**
     * Updates a Submission.
     *
     * @param id and dto
     * @return the updated DTO
     */
    Mono<SubmissionDTO> updateSubmission(UUID submissionId, SubmissionDTO dto);

    /**
     * Deletes a Submission.
     *
     * @param id identifier
     * @return completion signal
     */
    Mono<Void> deleteSubmission(UUID submissionId);

    /**
     * Loads a Submission by id.
     *
     * @param id identifier
     * @return the DTO
     */
    Mono<SubmissionDTO> getSubmissionById(UUID submissionId);
}
