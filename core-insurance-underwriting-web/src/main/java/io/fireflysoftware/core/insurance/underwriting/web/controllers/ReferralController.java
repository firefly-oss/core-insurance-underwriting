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
import io.fireflysoftware.core.insurance.underwriting.core.services.ReferralService;
import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.ReferralDTO;
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
 * REST API for referral of a submission to a person with delegated authority.
 */
@RestController
@RequestMapping("/api/v1/submissions/{submissionId}/referrals")
@RequiredArgsConstructor
@Validated
@Tag(name = "Referrals", description = "Referral of a submission to a person with delegated authority")
public class ReferralController {

    private final ReferralService referralService;

    @PostMapping("/filter")
    @Operation(summary = "Filter referrals", description = "Retrieve a page of referrals matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<ReferralDTO>>> filterReferrals(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<ReferralDTO> filterRequest) {
        return referralService.filterReferrals(submissionId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create Referral", description = "Create a Referral")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Referral created", content = @Content(schema = @Schema(implementation = ReferralDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<ReferralDTO>> createReferral(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Referral to create", required = true)
            @Valid @RequestBody ReferralDTO dto) {
        return referralService.createReferral(submissionId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{referralId}")
    @Operation(summary = "Get Referral by id", description = "Retrieve one Referral")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Referral found", content = @Content(schema = @Schema(implementation = ReferralDTO.class))),
        @ApiResponse(responseCode = "404", description = "Referral not found", content = @Content)
    })
    public Mono<ResponseEntity<ReferralDTO>> getReferralById(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the Referral", required = true)
            @PathVariable UUID referralId) {
        return referralService.getReferralById(submissionId, referralId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{referralId}")
    @Operation(summary = "Update Referral", description = "Update an existing Referral")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Referral updated", content = @Content(schema = @Schema(implementation = ReferralDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "Referral not found", content = @Content)
    })
    public Mono<ResponseEntity<ReferralDTO>> updateReferral(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the Referral", required = true)
            @PathVariable UUID referralId,
            @Parameter(description = "Updated Referral", required = true)
            @Valid @RequestBody ReferralDTO dto) {
        return referralService.updateReferral(submissionId, referralId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{referralId}")
    @Operation(summary = "Delete Referral", description = "Delete a Referral")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Referral deleted"),
        @ApiResponse(responseCode = "404", description = "Referral not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteReferral(
            @Parameter(description = "Unique identifier of the Submission", required = true)
            @PathVariable UUID submissionId,
            @Parameter(description = "Unique identifier of the Referral", required = true)
            @PathVariable UUID referralId) {
        return referralService.deleteReferral(submissionId, referralId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
