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

import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.LineOfBusiness;
import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.SubmissionStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.utils.annotations.FilterableId;

/**
 * Underwriting submission received for a product and applicant.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID submissionId;

    /** Logical reference: Firefly deployment tenant. This service does not own that record. */
    @FilterableId
    @NotNull(message = "tenantId is required")
    private UUID tenantId;

    @NotBlank(message = "submissionNumber is required")
    @Size(max = 64, message = "submissionNumber must not exceed 64 characters")
    private String submissionNumber;

    /** Logical reference: core-common-customer-mgmt party. This service does not own that record. */
    @FilterableId
    @NotNull(message = "partyId is required")
    private UUID partyId;

    /** Logical reference: core-common-product-mgmt product. This service does not own that record. */
    @FilterableId
    @NotNull(message = "productId is required")
    private UUID productId;

    /** Logical reference: core-common-distributor-mgmt distributor. This service does not own that record. */
    @FilterableId
    private UUID distributorId;

    /** Logical reference: core-common-contract-mgmt contract. This service does not own that record. */
    @FilterableId
    private UUID contractId;

    @NotNull(message = "submissionStatus is required")
    private SubmissionStatus submissionStatus;

    @NotNull(message = "lineOfBusiness is required")
    private LineOfBusiness lineOfBusiness;

    @NotNull(message = "receivedAt is required")
    private LocalDateTime receivedAt;

    @Pattern(regexp = "[A-Z]{3}", message = "currency must be a 3-letter ISO code")
    private String currency;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
