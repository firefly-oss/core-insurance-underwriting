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


package io.fireflysoftware.core.insurance.underwriting.core.services;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.QuoteDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for quote prepared from authorized terms. Rating stays outside this service..
 */
public interface QuoteService {

    /**
     * Filters quotes with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of QuoteDTO
     */
    Mono<PaginationResponse<QuoteDTO>> filterQuotes(UUID submissionId, FilterRequest<QuoteDTO> filterRequest);

    /**
     * Creates a Quote.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<QuoteDTO> createQuote(UUID submissionId, QuoteDTO dto);

    /**
     * Updates a Quote.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<QuoteDTO> updateQuote(UUID submissionId, UUID quoteId, QuoteDTO dto);

    /**
     * Deletes a Quote.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteQuote(UUID submissionId, UUID quoteId);

    /**
     * Loads a Quote by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<QuoteDTO> getQuoteById(UUID submissionId, UUID quoteId);
}
