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
import io.fireflysoftware.core.insurance.underwriting.core.services.LossRunService;
import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.LossRunDTO;
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
 * REST API for prior-year loss run summarized by line and cause.
 */
@RestController
@RequestMapping("/api/v1/submissions/{submissionId}/loss-runs")
@RequiredArgsConstructor
@Validated
@Tag(name = "Loss Runs", description = "Prior-year loss run summarized by line and cause")
public class LossRunController {

    private final LossRunService lossRunService;

    @PostMapping("/filter")
    @Operation(summary = "Filter lossRuns", description = "Retrieve a page of lossRuns matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<LossRunDTO>>> filterLossRuns(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<LossRunDTO> filterRequest) {
        return lossRunService.filterLossRuns(submissionId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create LossRun", description = "Create a LossRun")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "LossRun created", content = @Content(schema = @Schema(implementation = LossRunDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<LossRunDTO>> createLossRun(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "LossRun to create", required = true)
            @Valid @RequestBody LossRunDTO dto) {
        return lossRunService.createLossRun(submissionId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{lossRunId}")
    @Operation(summary = "Get LossRun by id", description = "Retrieve one LossRun")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "LossRun found", content = @Content(schema = @Schema(implementation = LossRunDTO.class))),
        @ApiResponse(responseCode = "404", description = "LossRun not found", content = @Content)
    })
    public Mono<ResponseEntity<LossRunDTO>> getLossRunById(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the LossRun", required = true)
            @PathVariable UUID lossRunId) {
        return lossRunService.getLossRunById(submissionId, lossRunId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{lossRunId}")
    @Operation(summary = "Update LossRun", description = "Update an existing LossRun")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "LossRun updated", content = @Content(schema = @Schema(implementation = LossRunDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "LossRun not found", content = @Content)
    })
    public Mono<ResponseEntity<LossRunDTO>> updateLossRun(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the LossRun", required = true)
            @PathVariable UUID lossRunId,
            @Parameter(description = "Updated LossRun", required = true)
            @Valid @RequestBody LossRunDTO dto) {
        return lossRunService.updateLossRun(submissionId, lossRunId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{lossRunId}")
    @Operation(summary = "Delete LossRun", description = "Delete a LossRun")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "LossRun deleted"),
        @ApiResponse(responseCode = "404", description = "LossRun not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteLossRun(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the LossRun", required = true)
            @PathVariable UUID lossRunId) {
        return lossRunService.deleteLossRun(submissionId, lossRunId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
