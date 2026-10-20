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
import io.fireflysoftware.core.insurance.underwriting.core.mappers.ReferralMapper;
import io.fireflysoftware.core.insurance.underwriting.core.services.ReferralService;
import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.ReferralDTO;
import io.fireflysoftware.core.insurance.underwriting.models.entities.Referral;
import io.fireflysoftware.core.insurance.underwriting.models.repositories.ReferralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class ReferralServiceImpl implements ReferralService {

    @Autowired
    private ReferralRepository repository;

    @Autowired
    private ReferralMapper mapper;

    @Override
    public Mono<PaginationResponse<ReferralDTO>> filterReferrals(UUID submissionId, FilterRequest<ReferralDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new ReferralDTO());
        }
        filterRequest.getFilters().setSubmissionId(submissionId);
        return FilterUtils.createFilter(Referral.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<ReferralDTO> createReferral(UUID submissionId, ReferralDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setSubmissionId(submissionId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ReferralDTO> updateReferral(UUID submissionId, UUID referralId, ReferralDTO dto) {
        return repository.findById(referralId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Referral", referralId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "Referral with id '" + referralId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteReferral(UUID submissionId, UUID referralId) {
        return repository.findById(referralId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Referral", referralId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "Referral with id '" + referralId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return repository.deleteById(referralId);
                });
    }

    @Override
    public Mono<ReferralDTO> getReferralById(UUID submissionId, UUID referralId) {
        return repository.findById(referralId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Referral", referralId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "Referral with id '" + referralId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
