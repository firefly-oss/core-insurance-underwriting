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
import io.fireflysoftware.core.insurance.underwriting.core.services.SubmissionStatusHistoryService;
import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.SubmissionStatusHistoryDTO;
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
 * REST API for status change recorded against a submission.
 */
@RestController
@RequestMapping("/api/v1/submissions/{submissionId}/status-history")
@RequiredArgsConstructor
@Validated
@Tag(name = "Submission Status History", description = "Status change recorded against a submission")
public class SubmissionStatusHistoryController {

    private final SubmissionStatusHistoryService submissionStatusHistoryService;

    @PostMapping("/filter")
    @Operation(summary = "Filter submissionStatusHistories", description = "Retrieve a page of submissionStatusHistories matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<SubmissionStatusHistoryDTO>>> filterSubmissionStatusHistories(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<SubmissionStatusHistoryDTO> filterRequest) {
        return submissionStatusHistoryService.filterSubmissionStatusHistories(submissionId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create SubmissionStatusHistory", description = "Create a SubmissionStatusHistory")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "SubmissionStatusHistory created", content = @Content(schema = @Schema(implementation = SubmissionStatusHistoryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<SubmissionStatusHistoryDTO>> createSubmissionStatusHistory(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "SubmissionStatusHistory to create", required = true)
            @Valid @RequestBody SubmissionStatusHistoryDTO dto) {
        return submissionStatusHistoryService.createSubmissionStatusHistory(submissionId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{submissionStatusHistoryId}")
    @Operation(summary = "Get SubmissionStatusHistory by id", description = "Retrieve one SubmissionStatusHistory")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "SubmissionStatusHistory found", content = @Content(schema = @Schema(implementation = SubmissionStatusHistoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "SubmissionStatusHistory not found", content = @Content)
    })
    public Mono<ResponseEntity<SubmissionStatusHistoryDTO>> getSubmissionStatusHistoryById(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the SubmissionStatusHistory", required = true)
            @PathVariable UUID submissionStatusHistoryId) {
        return submissionStatusHistoryService.getSubmissionStatusHistoryById(submissionId, submissionStatusHistoryId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{submissionStatusHistoryId}")
    @Operation(summary = "Update SubmissionStatusHistory", description = "Update an existing SubmissionStatusHistory")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "SubmissionStatusHistory updated", content = @Content(schema = @Schema(implementation = SubmissionStatusHistoryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "SubmissionStatusHistory not found", content = @Content)
    })
    public Mono<ResponseEntity<SubmissionStatusHistoryDTO>> updateSubmissionStatusHistory(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the SubmissionStatusHistory", required = true)
            @PathVariable UUID submissionStatusHistoryId,
            @Parameter(description = "Updated SubmissionStatusHistory", required = true)
            @Valid @RequestBody SubmissionStatusHistoryDTO dto) {
        return submissionStatusHistoryService.updateSubmissionStatusHistory(submissionId, submissionStatusHistoryId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{submissionStatusHistoryId}")
    @Operation(summary = "Delete SubmissionStatusHistory", description = "Delete a SubmissionStatusHistory")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "SubmissionStatusHistory deleted"),
        @ApiResponse(responseCode = "404", description = "SubmissionStatusHistory not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteSubmissionStatusHistory(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the SubmissionStatusHistory", required = true)
            @PathVariable UUID submissionStatusHistoryId) {
        return submissionStatusHistoryService.deleteSubmissionStatusHistory(submissionId, submissionStatusHistoryId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
