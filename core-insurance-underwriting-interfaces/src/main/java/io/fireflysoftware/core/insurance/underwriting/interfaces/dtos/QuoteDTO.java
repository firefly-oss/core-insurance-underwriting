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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.QuoteStatus;

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
 * Quote prepared from authorized terms. Rating stays outside this service..
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID quoteId;

    @FilterableId
    @NotNull(message = "Submission id is required")
    private UUID submissionId;

    @NotBlank(message = "quoteNumber is required")
    @Size(max = 64, message = "quoteNumber must not exceed 64 characters")
    private String quoteNumber;

    @NotNull(message = "quoteStatus is required")
    private QuoteStatus quoteStatus;

    private BigDecimal premiumAmount;

    @Pattern(regexp = "[A-Z]{3}", message = "currency must be a 3-letter ISO code")
    private String currency;

    private LocalDate validUntil;

    /** Logical reference: core-insurance-policies policy. This service does not own that record. */
    @FilterableId
    private UUID boundPolicyId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
