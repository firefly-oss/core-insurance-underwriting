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


package io.fireflysoftware.core.insurance.underwriting.interfaces.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.InsuredObjectType;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.utils.annotations.FilterableId;

/**
 * Risk item on a submission.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionRiskDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID submissionRiskId;

    @FilterableId
    @NotNull(message = "Submission id is required")
    private UUID submissionId;

    @NotBlank(message = "riskNumber is required")
    @Size(max = 64, message = "riskNumber must not exceed 64 characters")
    private String riskNumber;

    @Size(max = 4000, message = "description must not exceed 4000 characters")
    private String description;

    @NotNull(message = "insuredObjectType is required")
    private InsuredObjectType insuredObjectType;

    /** Logical reference: core-common-customer-mgmt party. This service does not own that record. */
    @FilterableId
    private UUID partyId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
