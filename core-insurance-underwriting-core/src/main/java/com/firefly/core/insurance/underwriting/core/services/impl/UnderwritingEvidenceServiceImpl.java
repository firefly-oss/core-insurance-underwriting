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


package com.firefly.core.insurance.underwriting.core.services.impl;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.web.error.exceptions.ResourceNotFoundException;
import com.firefly.core.insurance.underwriting.core.mappers.UnderwritingEvidenceMapper;
import com.firefly.core.insurance.underwriting.core.services.UnderwritingEvidenceService;
import com.firefly.core.insurance.underwriting.interfaces.dtos.UnderwritingEvidenceDTO;
import com.firefly.core.insurance.underwriting.models.entities.UnderwritingEvidence;
import com.firefly.core.insurance.underwriting.models.repositories.UnderwritingEvidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class UnderwritingEvidenceServiceImpl implements UnderwritingEvidenceService {

    @Autowired
    private UnderwritingEvidenceRepository repository;

    @Autowired
    private UnderwritingEvidenceMapper mapper;

    @Override
    public Mono<PaginationResponse<UnderwritingEvidenceDTO>> filterUnderwritingEvidenceItems(UUID submissionId, FilterRequest<UnderwritingEvidenceDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new UnderwritingEvidenceDTO());
        }
        filterRequest.getFilters().setSubmissionId(submissionId);
        return FilterUtils.createFilter(UnderwritingEvidence.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<UnderwritingEvidenceDTO> createUnderwritingEvidence(UUID submissionId, UnderwritingEvidenceDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setSubmissionId(submissionId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<UnderwritingEvidenceDTO> updateUnderwritingEvidence(UUID submissionId, UUID underwritingEvidenceId, UnderwritingEvidenceDTO dto) {
        return repository.findById(underwritingEvidenceId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("UnderwritingEvidence", underwritingEvidenceId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "UnderwritingEvidence with id '" + underwritingEvidenceId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteUnderwritingEvidence(UUID submissionId, UUID underwritingEvidenceId) {
        return repository.findById(underwritingEvidenceId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("UnderwritingEvidence", underwritingEvidenceId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "UnderwritingEvidence with id '" + underwritingEvidenceId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return repository.deleteById(underwritingEvidenceId);
                });
    }

    @Override
    public Mono<UnderwritingEvidenceDTO> getUnderwritingEvidenceById(UUID submissionId, UUID underwritingEvidenceId) {
        return repository.findById(underwritingEvidenceId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("UnderwritingEvidence", underwritingEvidenceId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "UnderwritingEvidence with id '" + underwritingEvidenceId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
