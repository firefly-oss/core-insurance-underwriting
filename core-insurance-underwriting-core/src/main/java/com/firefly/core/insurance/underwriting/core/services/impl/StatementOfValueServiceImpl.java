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
import com.firefly.core.insurance.underwriting.core.mappers.StatementOfValueMapper;
import com.firefly.core.insurance.underwriting.core.services.StatementOfValueService;
import com.firefly.core.insurance.underwriting.interfaces.dtos.StatementOfValueDTO;
import com.firefly.core.insurance.underwriting.models.entities.StatementOfValue;
import com.firefly.core.insurance.underwriting.models.repositories.StatementOfValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class StatementOfValueServiceImpl implements StatementOfValueService {

    @Autowired
    private StatementOfValueRepository repository;

    @Autowired
    private StatementOfValueMapper mapper;

    @Override
    public Mono<PaginationResponse<StatementOfValueDTO>> filterStatementsOfValue(UUID submissionId, FilterRequest<StatementOfValueDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new StatementOfValueDTO());
        }
        filterRequest.getFilters().setSubmissionId(submissionId);
        return FilterUtils.createFilter(StatementOfValue.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<StatementOfValueDTO> createStatementOfValue(UUID submissionId, StatementOfValueDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setSubmissionId(submissionId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<StatementOfValueDTO> updateStatementOfValue(UUID submissionId, UUID statementOfValueId, StatementOfValueDTO dto) {
        return repository.findById(statementOfValueId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("StatementOfValue", statementOfValueId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "StatementOfValue with id '" + statementOfValueId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteStatementOfValue(UUID submissionId, UUID statementOfValueId) {
        return repository.findById(statementOfValueId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("StatementOfValue", statementOfValueId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "StatementOfValue with id '" + statementOfValueId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return repository.deleteById(statementOfValueId);
                });
    }

    @Override
    public Mono<StatementOfValueDTO> getStatementOfValueById(UUID submissionId, UUID statementOfValueId) {
        return repository.findById(statementOfValueId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("StatementOfValue", statementOfValueId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "StatementOfValue with id '" + statementOfValueId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
