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
import io.fireflysoftware.core.insurance.underwriting.core.mappers.SanctionsScreenMapper;
import io.fireflysoftware.core.insurance.underwriting.core.services.SanctionsScreenService;
import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.SanctionsScreenDTO;
import io.fireflysoftware.core.insurance.underwriting.models.entities.SanctionsScreen;
import io.fireflysoftware.core.insurance.underwriting.models.repositories.SanctionsScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class SanctionsScreenServiceImpl implements SanctionsScreenService {

    @Autowired
    private SanctionsScreenRepository repository;

    @Autowired
    private SanctionsScreenMapper mapper;

    @Override
    public Mono<PaginationResponse<SanctionsScreenDTO>> filterSanctionsScreens(UUID submissionId, FilterRequest<SanctionsScreenDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new SanctionsScreenDTO());
        }
        filterRequest.getFilters().setSubmissionId(submissionId);
        return FilterUtils.createFilter(SanctionsScreen.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<SanctionsScreenDTO> createSanctionsScreen(UUID submissionId, SanctionsScreenDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setSubmissionId(submissionId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SanctionsScreenDTO> updateSanctionsScreen(UUID submissionId, UUID sanctionsScreenId, SanctionsScreenDTO dto) {
        return repository.findById(sanctionsScreenId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("SanctionsScreen", sanctionsScreenId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "SanctionsScreen with id '" + sanctionsScreenId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteSanctionsScreen(UUID submissionId, UUID sanctionsScreenId) {
        return repository.findById(sanctionsScreenId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("SanctionsScreen", sanctionsScreenId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "SanctionsScreen with id '" + sanctionsScreenId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return repository.deleteById(sanctionsScreenId);
                });
    }

    @Override
    public Mono<SanctionsScreenDTO> getSanctionsScreenById(UUID submissionId, UUID sanctionsScreenId) {
        return repository.findById(sanctionsScreenId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("SanctionsScreen", sanctionsScreenId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "SanctionsScreen with id '" + sanctionsScreenId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
