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
import com.firefly.core.insurance.underwriting.core.services.SanctionsScreenService;
import com.firefly.core.insurance.underwriting.interfaces.dtos.SanctionsScreenDTO;
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
 * REST API for recorded sanctions screening result. The check itself stays with the provider..
 */
@RestController
@RequestMapping("/api/v1/submissions/{submissionId}/sanctions-screens")
@RequiredArgsConstructor
@Validated
@Tag(name = "Sanctions Screens", description = "Recorded sanctions screening result. The check itself stays with the provider.")
public class SanctionsScreenController {

    private final SanctionsScreenService sanctionsScreenService;

    @PostMapping("/filter")
    @Operation(summary = "Filter sanctionsScreens", description = "Retrieve a page of sanctionsScreens matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<SanctionsScreenDTO>>> filterSanctionsScreens(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<SanctionsScreenDTO> filterRequest) {
        return sanctionsScreenService.filterSanctionsScreens(submissionId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create SanctionsScreen", description = "Create a SanctionsScreen")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "SanctionsScreen created", content = @Content(schema = @Schema(implementation = SanctionsScreenDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<SanctionsScreenDTO>> createSanctionsScreen(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "SanctionsScreen to create", required = true)
            @Valid @RequestBody SanctionsScreenDTO dto) {
        return sanctionsScreenService.createSanctionsScreen(submissionId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{sanctionsScreenId}")
    @Operation(summary = "Get SanctionsScreen by id", description = "Retrieve one SanctionsScreen")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "SanctionsScreen found", content = @Content(schema = @Schema(implementation = SanctionsScreenDTO.class))),
        @ApiResponse(responseCode = "404", description = "SanctionsScreen not found", content = @Content)
    })
    public Mono<ResponseEntity<SanctionsScreenDTO>> getSanctionsScreenById(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the SanctionsScreen", required = true)
            @PathVariable UUID sanctionsScreenId) {
        return sanctionsScreenService.getSanctionsScreenById(submissionId, sanctionsScreenId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{sanctionsScreenId}")
    @Operation(summary = "Update SanctionsScreen", description = "Update an existing SanctionsScreen")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "SanctionsScreen updated", content = @Content(schema = @Schema(implementation = SanctionsScreenDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "SanctionsScreen not found", content = @Content)
    })
    public Mono<ResponseEntity<SanctionsScreenDTO>> updateSanctionsScreen(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the SanctionsScreen", required = true)
            @PathVariable UUID sanctionsScreenId,
            @Parameter(description = "Updated SanctionsScreen", required = true)
            @Valid @RequestBody SanctionsScreenDTO dto) {
        return sanctionsScreenService.updateSanctionsScreen(submissionId, sanctionsScreenId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{sanctionsScreenId}")
    @Operation(summary = "Delete SanctionsScreen", description = "Delete a SanctionsScreen")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "SanctionsScreen deleted"),
        @ApiResponse(responseCode = "404", description = "SanctionsScreen not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteSanctionsScreen(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the SanctionsScreen", required = true)
            @PathVariable UUID sanctionsScreenId) {
        return sanctionsScreenService.deleteSanctionsScreen(submissionId, sanctionsScreenId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
