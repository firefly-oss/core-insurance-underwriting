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
import com.firefly.core.insurance.underwriting.core.services.SubjectivityService;
import com.firefly.core.insurance.underwriting.interfaces.dtos.SubjectivityDTO;
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
 * REST API for condition that must be cleared before bind.
 */
@RestController
@RequestMapping("/api/v1/submissions/{submissionId}/subjectivities")
@RequiredArgsConstructor
@Validated
@Tag(name = "Subjectivities", description = "Condition that must be cleared before bind")
public class SubjectivityController {

    private final SubjectivityService subjectivityService;

    @PostMapping("/filter")
    @Operation(summary = "Filter subjectivities", description = "Retrieve a page of subjectivities matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<SubjectivityDTO>>> filterSubjectivities(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<SubjectivityDTO> filterRequest) {
        return subjectivityService.filterSubjectivities(submissionId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create Subjectivity", description = "Create a Subjectivity")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Subjectivity created", content = @Content(schema = @Schema(implementation = SubjectivityDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<SubjectivityDTO>> createSubjectivity(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Subjectivity to create", required = true)
            @Valid @RequestBody SubjectivityDTO dto) {
        return subjectivityService.createSubjectivity(submissionId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{subjectivityId}")
    @Operation(summary = "Get Subjectivity by id", description = "Retrieve one Subjectivity")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subjectivity found", content = @Content(schema = @Schema(implementation = SubjectivityDTO.class))),
        @ApiResponse(responseCode = "404", description = "Subjectivity not found", content = @Content)
    })
    public Mono<ResponseEntity<SubjectivityDTO>> getSubjectivityById(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the Subjectivity", required = true)
            @PathVariable UUID subjectivityId) {
        return subjectivityService.getSubjectivityById(submissionId, subjectivityId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{subjectivityId}")
    @Operation(summary = "Update Subjectivity", description = "Update an existing Subjectivity")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subjectivity updated", content = @Content(schema = @Schema(implementation = SubjectivityDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "Subjectivity not found", content = @Content)
    })
    public Mono<ResponseEntity<SubjectivityDTO>> updateSubjectivity(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the Subjectivity", required = true)
            @PathVariable UUID subjectivityId,
            @Parameter(description = "Updated Subjectivity", required = true)
            @Valid @RequestBody SubjectivityDTO dto) {
        return subjectivityService.updateSubjectivity(submissionId, subjectivityId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{subjectivityId}")
    @Operation(summary = "Delete Subjectivity", description = "Delete a Subjectivity")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Subjectivity deleted"),
        @ApiResponse(responseCode = "404", description = "Subjectivity not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteSubjectivity(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the Subjectivity", required = true)
            @PathVariable UUID subjectivityId) {
        return subjectivityService.deleteSubjectivity(submissionId, subjectivityId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
