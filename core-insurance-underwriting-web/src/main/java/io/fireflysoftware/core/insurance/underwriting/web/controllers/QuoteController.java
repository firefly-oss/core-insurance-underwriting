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


package io.fireflysoftware.core.insurance.underwriting.web.controllers;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.insurance.underwriting.core.services.QuoteService;
import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.QuoteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * REST API for quote prepared from authorized terms. Rating stays outside this service..
 */
@RestController
@RequestMapping("/api/v1/submissions/{submissionId}/quotes")
@RequiredArgsConstructor
@Validated
@Tag(name = "Quotes", description = "Quote prepared from authorized terms. Rating stays outside this service.")
public class QuoteController {

    private final QuoteService quoteService;

    @PostMapping("/filter")
    @Operation(summary = "Filter quotes", description = "Retrieve a page of quotes matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<QuoteDTO>>> filterQuotes(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<QuoteDTO> filterRequest) {
        return quoteService.filterQuotes(submissionId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create Quote", description = "Create a Quote")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Quote created", content = @Content(schema = @Schema(implementation = QuoteDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<QuoteDTO>> createQuote(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Quote to create", required = true)
            @Valid @RequestBody QuoteDTO dto) {
        return quoteService.createQuote(submissionId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{quoteId}")
    @Operation(summary = "Get Quote by id", description = "Retrieve one Quote")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quote found", content = @Content(schema = @Schema(implementation = QuoteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Quote not found", content = @Content)
    })
    public Mono<ResponseEntity<QuoteDTO>> getQuoteById(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the Quote", required = true)
            @PathVariable UUID quoteId) {
        return quoteService.getQuoteById(submissionId, quoteId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{quoteId}")
    @Operation(summary = "Update Quote", description = "Update an existing Quote")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quote updated", content = @Content(schema = @Schema(implementation = QuoteDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "Quote not found", content = @Content)
    })
    public Mono<ResponseEntity<QuoteDTO>> updateQuote(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the Quote", required = true)
            @PathVariable UUID quoteId,
            @Parameter(description = "Updated Quote", required = true)
            @Valid @RequestBody QuoteDTO dto) {
        return quoteService.updateQuote(submissionId, quoteId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{quoteId}")
    @Operation(summary = "Delete Quote", description = "Delete a Quote")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Quote deleted"),
        @ApiResponse(responseCode = "404", description = "Quote not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteQuote(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the Quote", required = true)
            @PathVariable UUID quoteId) {
        return quoteService.deleteQuote(submissionId, quoteId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
