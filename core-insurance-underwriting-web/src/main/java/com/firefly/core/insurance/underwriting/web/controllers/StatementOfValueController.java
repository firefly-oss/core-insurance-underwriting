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


package com.firefly.core.insurance.underwriting.web.controllers;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.insurance.underwriting.core.services.StatementOfValueService;
import com.firefly.core.insurance.underwriting.interfaces.dtos.StatementOfValueDTO;
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
 * REST API for one normalized statement-of-values line on a submission.
 */
@RestController
@RequestMapping("/api/v1/submissions/{submissionId}/statements-of-value")
@RequiredArgsConstructor
@Validated
@Tag(name = "Statements of Value", description = "One normalized statement-of-values line on a submission")
public class StatementOfValueController {

    private final StatementOfValueService statementOfValueService;

    @PostMapping("/filter")
    @Operation(summary = "Filter statementsOfValue", description = "Retrieve a page of statementsOfValue matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<StatementOfValueDTO>>> filterStatementsOfValue(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<StatementOfValueDTO> filterRequest) {
        return statementOfValueService.filterStatementsOfValue(submissionId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create StatementOfValue", description = "Create a StatementOfValue")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "StatementOfValue created", content = @Content(schema = @Schema(implementation = StatementOfValueDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<StatementOfValueDTO>> createStatementOfValue(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "StatementOfValue to create", required = true)
            @Valid @RequestBody StatementOfValueDTO dto) {
        return statementOfValueService.createStatementOfValue(submissionId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{statementOfValueId}")
    @Operation(summary = "Get StatementOfValue by id", description = "Retrieve one StatementOfValue")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "StatementOfValue found", content = @Content(schema = @Schema(implementation = StatementOfValueDTO.class))),
        @ApiResponse(responseCode = "404", description = "StatementOfValue not found", content = @Content)
    })
    public Mono<ResponseEntity<StatementOfValueDTO>> getStatementOfValueById(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the StatementOfValue", required = true)
            @PathVariable UUID statementOfValueId) {
        return statementOfValueService.getStatementOfValueById(submissionId, statementOfValueId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{statementOfValueId}")
    @Operation(summary = "Update StatementOfValue", description = "Update an existing StatementOfValue")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "StatementOfValue updated", content = @Content(schema = @Schema(implementation = StatementOfValueDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "StatementOfValue not found", content = @Content)
    })
    public Mono<ResponseEntity<StatementOfValueDTO>> updateStatementOfValue(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the StatementOfValue", required = true)
            @PathVariable UUID statementOfValueId,
            @Parameter(description = "Updated StatementOfValue", required = true)
            @Valid @RequestBody StatementOfValueDTO dto) {
        return statementOfValueService.updateStatementOfValue(submissionId, statementOfValueId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{statementOfValueId}")
    @Operation(summary = "Delete StatementOfValue", description = "Delete a StatementOfValue")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "StatementOfValue deleted"),
        @ApiResponse(responseCode = "404", description = "StatementOfValue not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteStatementOfValue(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the StatementOfValue", required = true)
            @PathVariable UUID statementOfValueId) {
        return statementOfValueService.deleteStatementOfValue(submissionId, statementOfValueId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
