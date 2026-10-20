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
import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.SubjectivityDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for condition that must be cleared before bind.
 */
public interface SubjectivityService {

    /**
     * Filters subjectivities with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of SubjectivityDTO
     */
    Mono<PaginationResponse<SubjectivityDTO>> filterSubjectivities(UUID submissionId, FilterRequest<SubjectivityDTO> filterRequest);

    /**
     * Creates a Subjectivity.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<SubjectivityDTO> createSubjectivity(UUID submissionId, SubjectivityDTO dto);

    /**
     * Updates a Subjectivity.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<SubjectivityDTO> updateSubjectivity(UUID submissionId, UUID subjectivityId, SubjectivityDTO dto);

    /**
     * Deletes a Subjectivity.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteSubjectivity(UUID submissionId, UUID subjectivityId);

    /**
     * Loads a Subjectivity by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<SubjectivityDTO> getSubjectivityById(UUID submissionId, UUID subjectivityId);
}
