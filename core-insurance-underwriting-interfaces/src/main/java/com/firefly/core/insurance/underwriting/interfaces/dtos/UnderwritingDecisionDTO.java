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


package com.firefly.core.insurance.underwriting.interfaces.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.firefly.core.insurance.underwriting.interfaces.enums.DecisionOutcome;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.utils.annotations.FilterableId;

/**
 * Recorded underwriting decision. Authority stays with the insurer..
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnderwritingDecisionDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID underwritingDecisionId;

    @FilterableId
    @NotNull(message = "Submission id is required")
    private UUID submissionId;

    @NotNull(message = "decisionOutcome is required")
    private DecisionOutcome decisionOutcome;

    @Size(max = 4000, message = "conditions must not exceed 4000 characters")
    private String conditions;

    @NotNull(message = "decidedAt is required")
    private LocalDateTime decidedAt;

    @Size(max = 200, message = "authorityReference must not exceed 200 characters")
    private String authorityReference;

    /** Logical reference: core-insurance-policies policy. This service does not own that record. */
    @FilterableId
    private UUID boundPolicyId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
