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
import com.firefly.core.insurance.underwriting.core.services.UnderwritingDecisionService;
import com.firefly.core.insurance.underwriting.interfaces.dtos.UnderwritingDecisionDTO;
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
 * REST API for recorded underwriting decision. Authority stays with the insurer..
 */
@RestController
@RequestMapping("/api/v1/submissions/{submissionId}/decisions")
@RequiredArgsConstructor
@Validated
@Tag(name = "Underwriting Decisions", description = "Recorded underwriting decision. Authority stays with the insurer.")
public class UnderwritingDecisionController {

    private final UnderwritingDecisionService underwritingDecisionService;

    @PostMapping("/filter")
    @Operation(summary = "Filter underwritingDecisions", description = "Retrieve a page of underwritingDecisions matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<UnderwritingDecisionDTO>>> filterUnderwritingDecisions(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<UnderwritingDecisionDTO> filterRequest) {
        return underwritingDecisionService.filterUnderwritingDecisions(submissionId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create UnderwritingDecision", description = "Create a UnderwritingDecision")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "UnderwritingDecision created", content = @Content(schema = @Schema(implementation = UnderwritingDecisionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<UnderwritingDecisionDTO>> createUnderwritingDecision(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "UnderwritingDecision to create", required = true)
            @Valid @RequestBody UnderwritingDecisionDTO dto) {
        return underwritingDecisionService.createUnderwritingDecision(submissionId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{underwritingDecisionId}")
    @Operation(summary = "Get UnderwritingDecision by id", description = "Retrieve one UnderwritingDecision")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "UnderwritingDecision found", content = @Content(schema = @Schema(implementation = UnderwritingDecisionDTO.class))),
        @ApiResponse(responseCode = "404", description = "UnderwritingDecision not found", content = @Content)
    })
    public Mono<ResponseEntity<UnderwritingDecisionDTO>> getUnderwritingDecisionById(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the UnderwritingDecision", required = true)
            @PathVariable UUID underwritingDecisionId) {
        return underwritingDecisionService.getUnderwritingDecisionById(submissionId, underwritingDecisionId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{underwritingDecisionId}")
    @Operation(summary = "Update UnderwritingDecision", description = "Update an existing UnderwritingDecision")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "UnderwritingDecision updated", content = @Content(schema = @Schema(implementation = UnderwritingDecisionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "UnderwritingDecision not found", content = @Content)
    })
    public Mono<ResponseEntity<UnderwritingDecisionDTO>> updateUnderwritingDecision(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the UnderwritingDecision", required = true)
            @PathVariable UUID underwritingDecisionId,
            @Parameter(description = "Updated UnderwritingDecision", required = true)
            @Valid @RequestBody UnderwritingDecisionDTO dto) {
        return underwritingDecisionService.updateUnderwritingDecision(submissionId, underwritingDecisionId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{underwritingDecisionId}")
    @Operation(summary = "Delete UnderwritingDecision", description = "Delete a UnderwritingDecision")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "UnderwritingDecision deleted"),
        @ApiResponse(responseCode = "404", description = "UnderwritingDecision not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteUnderwritingDecision(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the UnderwritingDecision", required = true)
            @PathVariable UUID underwritingDecisionId) {
        return underwritingDecisionService.deleteUnderwritingDecision(submissionId, underwritingDecisionId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
