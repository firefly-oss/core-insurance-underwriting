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
import com.firefly.core.insurance.underwriting.core.mappers.ClearanceMapper;
import com.firefly.core.insurance.underwriting.core.services.ClearanceService;
import com.firefly.core.insurance.underwriting.interfaces.dtos.ClearanceDTO;
import com.firefly.core.insurance.underwriting.models.entities.Clearance;
import com.firefly.core.insurance.underwriting.models.repositories.ClearanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class ClearanceServiceImpl implements ClearanceService {

    @Autowired
    private ClearanceRepository repository;

    @Autowired
    private ClearanceMapper mapper;

    @Override
    public Mono<PaginationResponse<ClearanceDTO>> filterClearances(UUID submissionId, FilterRequest<ClearanceDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new ClearanceDTO());
        }
        filterRequest.getFilters().setSubmissionId(submissionId);
        return FilterUtils.createFilter(Clearance.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<ClearanceDTO> createClearance(UUID submissionId, ClearanceDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setSubmissionId(submissionId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ClearanceDTO> updateClearance(UUID submissionId, UUID clearanceId, ClearanceDTO dto) {
        return repository.findById(clearanceId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Clearance", clearanceId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "Clearance with id '" + clearanceId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteClearance(UUID submissionId, UUID clearanceId) {
        return repository.findById(clearanceId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Clearance", clearanceId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "Clearance with id '" + clearanceId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return repository.deleteById(clearanceId);
                });
    }

    @Override
    public Mono<ClearanceDTO> getClearanceById(UUID submissionId, UUID clearanceId) {
        return repository.findById(clearanceId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Clearance", clearanceId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "Clearance with id '" + clearanceId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
