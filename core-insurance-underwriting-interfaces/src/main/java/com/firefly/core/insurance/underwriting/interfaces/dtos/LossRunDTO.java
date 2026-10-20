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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.firefly.core.insurance.underwriting.interfaces.enums.LineOfBusiness;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.utils.annotations.FilterableId;

/**
 * Prior-year loss run summarized by line and cause.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LossRunDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID lossRunId;

    @FilterableId
    @NotNull(message = "Submission id is required")
    private UUID submissionId;

    @Size(max = 255, message = "priorCarrierName must not exceed 255 characters")
    private String priorCarrierName;

    private LineOfBusiness lineOfBusiness;

    @NotNull(message = "lossYear is required")
    private Integer lossYear;

    @Size(max = 64, message = "causeCode must not exceed 64 characters")
    private String causeCode;

    private Integer claimCount;

    private BigDecimal incurredAmount;

    @Pattern(regexp = "[A-Z]{3}", message = "currency must be a 3-letter ISO code")
    private String currency;

    private Boolean largeLoss;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
