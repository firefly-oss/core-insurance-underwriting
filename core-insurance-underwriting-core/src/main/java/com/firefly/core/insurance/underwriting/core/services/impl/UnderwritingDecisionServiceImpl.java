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
import com.firefly.core.insurance.underwriting.core.mappers.UnderwritingDecisionMapper;
import com.firefly.core.insurance.underwriting.core.services.UnderwritingDecisionService;
import com.firefly.core.insurance.underwriting.interfaces.dtos.UnderwritingDecisionDTO;
import com.firefly.core.insurance.underwriting.models.entities.UnderwritingDecision;
import com.firefly.core.insurance.underwriting.models.repositories.UnderwritingDecisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class UnderwritingDecisionServiceImpl implements UnderwritingDecisionService {

    @Autowired
    private UnderwritingDecisionRepository repository;

    @Autowired
    private UnderwritingDecisionMapper mapper;

    @Override
    public Mono<PaginationResponse<UnderwritingDecisionDTO>> filterUnderwritingDecisions(UUID submissionId, FilterRequest<UnderwritingDecisionDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new UnderwritingDecisionDTO());
        }
        filterRequest.getFilters().setSubmissionId(submissionId);
        return FilterUtils.createFilter(UnderwritingDecision.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<UnderwritingDecisionDTO> createUnderwritingDecision(UUID submissionId, UnderwritingDecisionDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setSubmissionId(submissionId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<UnderwritingDecisionDTO> updateUnderwritingDecision(UUID submissionId, UUID underwritingDecisionId, UnderwritingDecisionDTO dto) {
        return repository.findById(underwritingDecisionId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("UnderwritingDecision", underwritingDecisionId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "UnderwritingDecision with id '" + underwritingDecisionId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteUnderwritingDecision(UUID submissionId, UUID underwritingDecisionId) {
        return repository.findById(underwritingDecisionId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("UnderwritingDecision", underwritingDecisionId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "UnderwritingDecision with id '" + underwritingDecisionId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return repository.deleteById(underwritingDecisionId);
                });
    }

    @Override
    public Mono<UnderwritingDecisionDTO> getUnderwritingDecisionById(UUID submissionId, UUID underwritingDecisionId) {
        return repository.findById(underwritingDecisionId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("UnderwritingDecision", underwritingDecisionId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "UnderwritingDecision with id '" + underwritingDecisionId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
