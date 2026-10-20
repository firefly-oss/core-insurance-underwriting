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
import com.firefly.core.insurance.underwriting.core.mappers.QuoteMapper;
import com.firefly.core.insurance.underwriting.core.services.QuoteService;
import com.firefly.core.insurance.underwriting.interfaces.dtos.QuoteDTO;
import com.firefly.core.insurance.underwriting.models.entities.Quote;
import com.firefly.core.insurance.underwriting.models.repositories.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class QuoteServiceImpl implements QuoteService {

    @Autowired
    private QuoteRepository repository;

    @Autowired
    private QuoteMapper mapper;

    @Override
    public Mono<PaginationResponse<QuoteDTO>> filterQuotes(UUID submissionId, FilterRequest<QuoteDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new QuoteDTO());
        }
        filterRequest.getFilters().setSubmissionId(submissionId);
        return FilterUtils.createFilter(Quote.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<QuoteDTO> createQuote(UUID submissionId, QuoteDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setSubmissionId(submissionId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<QuoteDTO> updateQuote(UUID submissionId, UUID quoteId, QuoteDTO dto) {
        return repository.findById(quoteId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Quote", quoteId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "Quote with id '" + quoteId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteQuote(UUID submissionId, UUID quoteId) {
        return repository.findById(quoteId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Quote", quoteId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "Quote with id '" + quoteId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return repository.deleteById(quoteId);
                });
    }

    @Override
    public Mono<QuoteDTO> getQuoteById(UUID submissionId, UUID quoteId) {
        return repository.findById(quoteId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Quote", quoteId.toString())))
                .flatMap(existing -> {
                    if (!submissionId.equals(existing.getSubmissionId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "Quote with id '" + quoteId + "' does not belong to submission '" + submissionId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
