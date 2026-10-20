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
import com.firefly.core.insurance.underwriting.core.mappers.SubmissionStatusHistoryMapper;
import com.firefly.core.insurance.underwriting.core.services.SubmissionStatusHistoryService;
import com.firefly.core.insurance.underwriting.interfaces.dtos.SubmissionStatusHistoryDTO;
import com.firefly.core.insurance.underwriting.models.entities.SubmissionStatusHistory;
import com.firefly.core.insurance.underwriting.models.repositories.SubmissionStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class SubmissionStatusHistoryServiceImpl implements SubmissionStatusHistoryService {

    @Autowired
    private SubmissionStatusHistoryRepository repository;

    @Autowired
    private SubmissionStatusHistoryMapper mapper;

    @Override
    public Mono<PaginationResponse<SubmissionStatusHistoryDTO>> filterSubmissionStatusHistories(UUID submissionId, FilterRequest<SubmissionStatusHistoryDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new SubmissionStatusHistoryDTO());
        }
        filterRequest.getFilters().setSubmissionId(submissionId);
        return FilterUtils.createFilter(SubmissionStatusHistory.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<SubmissionStatusHistoryDTO> createSubmissionStatusHistory(UUID submissionId, SubmissionStatusHistoryDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setSubmissionId(submissionId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SubmissionStatusHistoryDTO> updateSubmissionStatusHistory(UUID submissionId, UUID submissionStatusHistoryId, SubmissionStatusHistoryDTO dto) {
        return repository.findById(submissionStatusHistoryId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("SubmissionStatusHistory", submissionStatusHistoryId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "SubmissionStatusHistory with id '" + submissionStatusHistoryId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteSubmissionStatusHistory(UUID submissionId, UUID submissionStatusHistoryId) {
        return repository.findById(submissionStatusHistoryId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("SubmissionStatusHistory", submissionStatusHistoryId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "SubmissionStatusHistory with id '" + submissionStatusHistoryId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return repository.deleteById(submissionStatusHistoryId);
                });
    }

    @Override
    public Mono<SubmissionStatusHistoryDTO> getSubmissionStatusHistoryById(UUID submissionId, UUID submissionStatusHistoryId) {
        return repository.findById(submissionStatusHistoryId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("SubmissionStatusHistory", submissionStatusHistoryId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "SubmissionStatusHistory with id '" + submissionStatusHistoryId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
