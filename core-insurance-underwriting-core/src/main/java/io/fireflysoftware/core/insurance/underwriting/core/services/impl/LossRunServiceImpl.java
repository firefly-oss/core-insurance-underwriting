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
import io.fireflysoftware.core.insurance.underwriting.core.mappers.LossRunMapper;
import io.fireflysoftware.core.insurance.underwriting.core.services.LossRunService;
import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.LossRunDTO;
import io.fireflysoftware.core.insurance.underwriting.models.entities.LossRun;
import io.fireflysoftware.core.insurance.underwriting.models.repositories.LossRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class LossRunServiceImpl implements LossRunService {

    @Autowired
    private LossRunRepository repository;

    @Autowired
    private LossRunMapper mapper;

    @Override
    public Mono<PaginationResponse<LossRunDTO>> filterLossRuns(UUID submissionId, FilterRequest<LossRunDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new LossRunDTO());
        }
        filterRequest.getFilters().setSubmissionId(submissionId);
        return FilterUtils.createFilter(LossRun.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<LossRunDTO> createLossRun(UUID submissionId, LossRunDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setSubmissionId(submissionId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<LossRunDTO> updateLossRun(UUID submissionId, UUID lossRunId, LossRunDTO dto) {
        return repository.findById(lossRunId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("LossRun", lossRunId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "LossRun with id '" + lossRunId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteLossRun(UUID submissionId, UUID lossRunId) {
        return repository.findById(lossRunId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("LossRun", lossRunId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "LossRun with id '" + lossRunId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return repository.deleteById(lossRunId);
                });
    }

    @Override
    public Mono<LossRunDTO> getLossRunById(UUID submissionId, UUID lossRunId) {
        return repository.findById(lossRunId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("LossRun", lossRunId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "LossRun with id '" + lossRunId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
