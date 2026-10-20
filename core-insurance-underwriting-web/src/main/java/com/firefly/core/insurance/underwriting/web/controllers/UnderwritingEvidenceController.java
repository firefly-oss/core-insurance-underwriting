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
import com.firefly.core.insurance.underwriting.core.services.UnderwritingEvidenceService;
import com.firefly.core.insurance.underwriting.interfaces.dtos.UnderwritingEvidenceDTO;
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
 * REST API for evidence reference on a submission. The document bytes stay in document management..
 */
@RestController
@RequestMapping("/api/v1/submissions/{submissionId}/evidence")
@RequiredArgsConstructor
@Validated
@Tag(name = "Underwriting Evidence", description = "Evidence reference on a submission. The document bytes stay in document management.")
public class UnderwritingEvidenceController {

    private final UnderwritingEvidenceService underwritingEvidenceService;

    @PostMapping("/filter")
    @Operation(summary = "Filter underwritingEvidenceItems", description = "Retrieve a page of underwritingEvidenceItems matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<UnderwritingEvidenceDTO>>> filterUnderwritingEvidenceItems(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<UnderwritingEvidenceDTO> filterRequest) {
        return underwritingEvidenceService.filterUnderwritingEvidenceItems(submissionId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create UnderwritingEvidence", description = "Create a UnderwritingEvidence")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "UnderwritingEvidence created", content = @Content(schema = @Schema(implementation = UnderwritingEvidenceDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<UnderwritingEvidenceDTO>> createUnderwritingEvidence(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "UnderwritingEvidence to create", required = true)
            @Valid @RequestBody UnderwritingEvidenceDTO dto) {
        return underwritingEvidenceService.createUnderwritingEvidence(submissionId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{underwritingEvidenceId}")
    @Operation(summary = "Get UnderwritingEvidence by id", description = "Retrieve one UnderwritingEvidence")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "UnderwritingEvidence found", content = @Content(schema = @Schema(implementation = UnderwritingEvidenceDTO.class))),
        @ApiResponse(responseCode = "404", description = "UnderwritingEvidence not found", content = @Content)
    })
    public Mono<ResponseEntity<UnderwritingEvidenceDTO>> getUnderwritingEvidenceById(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the UnderwritingEvidence", required = true)
            @PathVariable UUID underwritingEvidenceId) {
        return underwritingEvidenceService.getUnderwritingEvidenceById(submissionId, underwritingEvidenceId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{underwritingEvidenceId}")
    @Operation(summary = "Update UnderwritingEvidence", description = "Update an existing UnderwritingEvidence")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "UnderwritingEvidence updated", content = @Content(schema = @Schema(implementation = UnderwritingEvidenceDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "UnderwritingEvidence not found", content = @Content)
    })
    public Mono<ResponseEntity<UnderwritingEvidenceDTO>> updateUnderwritingEvidence(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the UnderwritingEvidence", required = true)
            @PathVariable UUID underwritingEvidenceId,
            @Parameter(description = "Updated UnderwritingEvidence", required = true)
            @Valid @RequestBody UnderwritingEvidenceDTO dto) {
        return underwritingEvidenceService.updateUnderwritingEvidence(submissionId, underwritingEvidenceId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{underwritingEvidenceId}")
    @Operation(summary = "Delete UnderwritingEvidence", description = "Delete a UnderwritingEvidence")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "UnderwritingEvidence deleted"),
        @ApiResponse(responseCode = "404", description = "UnderwritingEvidence not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteUnderwritingEvidence(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the UnderwritingEvidence", required = true)
            @PathVariable UUID underwritingEvidenceId) {
        return underwritingEvidenceService.deleteUnderwritingEvidence(submissionId, underwritingEvidenceId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
