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
import com.firefly.core.insurance.underwriting.core.services.SubmissionRiskService;
import com.firefly.core.insurance.underwriting.interfaces.dtos.SubmissionRiskDTO;
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
 * REST API for risk item on a submission.
 */
@RestController
@RequestMapping("/api/v1/submissions/{submissionId}/risks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Submission Risks", description = "Risk item on a submission")
public class SubmissionRiskController {

    private final SubmissionRiskService submissionRiskService;

    @PostMapping("/filter")
    @Operation(summary = "Filter submissionRisks", description = "Retrieve a page of submissionRisks matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<SubmissionRiskDTO>>> filterSubmissionRisks(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<SubmissionRiskDTO> filterRequest) {
        return submissionRiskService.filterSubmissionRisks(submissionId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create SubmissionRisk", description = "Create a SubmissionRisk")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "SubmissionRisk created", content = @Content(schema = @Schema(implementation = SubmissionRiskDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<SubmissionRiskDTO>> createSubmissionRisk(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "SubmissionRisk to create", required = true)
            @Valid @RequestBody SubmissionRiskDTO dto) {
        return submissionRiskService.createSubmissionRisk(submissionId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{submissionRiskId}")
    @Operation(summary = "Get SubmissionRisk by id", description = "Retrieve one SubmissionRisk")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "SubmissionRisk found", content = @Content(schema = @Schema(implementation = SubmissionRiskDTO.class))),
        @ApiResponse(responseCode = "404", description = "SubmissionRisk not found", content = @Content)
    })
    public Mono<ResponseEntity<SubmissionRiskDTO>> getSubmissionRiskById(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the SubmissionRisk", required = true)
            @PathVariable UUID submissionRiskId) {
        return submissionRiskService.getSubmissionRiskById(submissionId, submissionRiskId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{submissionRiskId}")
    @Operation(summary = "Update SubmissionRisk", description = "Update an existing SubmissionRisk")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "SubmissionRisk updated", content = @Content(schema = @Schema(implementation = SubmissionRiskDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "SubmissionRisk not found", content = @Content)
    })
    public Mono<ResponseEntity<SubmissionRiskDTO>> updateSubmissionRisk(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the SubmissionRisk", required = true)
            @PathVariable UUID submissionRiskId,
            @Parameter(description = "Updated SubmissionRisk", required = true)
            @Valid @RequestBody SubmissionRiskDTO dto) {
        return submissionRiskService.updateSubmissionRisk(submissionId, submissionRiskId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{submissionRiskId}")
    @Operation(summary = "Delete SubmissionRisk", description = "Delete a SubmissionRisk")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "SubmissionRisk deleted"),
        @ApiResponse(responseCode = "404", description = "SubmissionRisk not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteSubmissionRisk(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the SubmissionRisk", required = true)
            @PathVariable UUID submissionRiskId) {
        return submissionRiskService.deleteSubmissionRisk(submissionId, submissionRiskId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
