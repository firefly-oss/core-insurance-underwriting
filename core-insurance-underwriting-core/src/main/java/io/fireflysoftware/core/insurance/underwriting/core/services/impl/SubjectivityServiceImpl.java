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


package io.fireflysoftware.core.insurance.underwriting.core.services.impl;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.web.error.exceptions.ResourceNotFoundException;
import io.fireflysoftware.core.insurance.underwriting.core.mappers.SubjectivityMapper;
import io.fireflysoftware.core.insurance.underwriting.core.services.SubjectivityService;
import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.SubjectivityDTO;
import io.fireflysoftware.core.insurance.underwriting.models.entities.Subjectivity;
import io.fireflysoftware.core.insurance.underwriting.models.repositories.SubjectivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class SubjectivityServiceImpl implements SubjectivityService {

    @Autowired
    private SubjectivityRepository repository;

    @Autowired
    private SubjectivityMapper mapper;

    @Override
    public Mono<PaginationResponse<SubjectivityDTO>> filterSubjectivities(UUID submissionId, FilterRequest<SubjectivityDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new SubjectivityDTO());
        }
        filterRequest.getFilters().setSubmissionId(submissionId);
        return FilterUtils.createFilter(Subjectivity.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<SubjectivityDTO> createSubjectivity(UUID submissionId, SubjectivityDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setSubmissionId(submissionId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SubjectivityDTO> updateSubjectivity(UUID submissionId, UUID subjectivityId, SubjectivityDTO dto) {
        return repository.findById(subjectivityId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Subjectivity", subjectivityId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "Subjectivity with id '" + subjectivityId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteSubjectivity(UUID submissionId, UUID subjectivityId) {
        return repository.findById(subjectivityId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Subjectivity", subjectivityId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "Subjectivity with id '" + subjectivityId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return repository.deleteById(subjectivityId);
                });
    }

    @Override
    public Mono<SubjectivityDTO> getSubjectivityById(UUID submissionId, UUID subjectivityId) {
        return repository.findById(subjectivityId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Subjectivity", subjectivityId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "Subjectivity with id '" + subjectivityId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
