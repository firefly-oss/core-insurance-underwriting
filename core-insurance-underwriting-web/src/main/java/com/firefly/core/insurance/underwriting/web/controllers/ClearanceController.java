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
import com.firefly.core.insurance.underwriting.core.services.ClearanceService;
import com.firefly.core.insurance.underwriting.interfaces.dtos.ClearanceDTO;
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
 * REST API for clearance of a submission against in-force business and the broker of record.
 */
@RestController
@RequestMapping("/api/v1/submissions/{submissionId}/clearances")
@RequiredArgsConstructor
@Validated
@Tag(name = "Clearances", description = "Clearance of a submission against in-force business and the broker of record")
public class ClearanceController {

    private final ClearanceService clearanceService;

    @PostMapping("/filter")
    @Operation(summary = "Filter clearances", description = "Retrieve a page of clearances matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<ClearanceDTO>>> filterClearances(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<ClearanceDTO> filterRequest) {
        return clearanceService.filterClearances(submissionId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create Clearance", description = "Create a Clearance")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Clearance created", content = @Content(schema = @Schema(implementation = ClearanceDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<ClearanceDTO>> createClearance(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Clearance to create", required = true)
            @Valid @RequestBody ClearanceDTO dto) {
        return clearanceService.createClearance(submissionId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{clearanceId}")
    @Operation(summary = "Get Clearance by id", description = "Retrieve one Clearance")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Clearance found", content = @Content(schema = @Schema(implementation = ClearanceDTO.class))),
        @ApiResponse(responseCode = "404", description = "Clearance not found", content = @Content)
    })
    public Mono<ResponseEntity<ClearanceDTO>> getClearanceById(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the Clearance", required = true)
            @PathVariable UUID clearanceId) {
        return clearanceService.getClearanceById(submissionId, clearanceId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{clearanceId}")
    @Operation(summary = "Update Clearance", description = "Update an existing Clearance")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Clearance updated", content = @Content(schema = @Schema(implementation = ClearanceDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "Clearance not found", content = @Content)
    })
    public Mono<ResponseEntity<ClearanceDTO>> updateClearance(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the Clearance", required = true)
            @PathVariable UUID clearanceId,
            @Parameter(description = "Updated Clearance", required = true)
            @Valid @RequestBody ClearanceDTO dto) {
        return clearanceService.updateClearance(submissionId, clearanceId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{clearanceId}")
    @Operation(summary = "Delete Clearance", description = "Delete a Clearance")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Clearance deleted"),
        @ApiResponse(responseCode = "404", description = "Clearance not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteClearance(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the Clearance", required = true)
            @PathVariable UUID clearanceId) {
        return clearanceService.deleteClearance(submissionId, clearanceId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
