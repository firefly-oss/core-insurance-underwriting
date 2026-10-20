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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.firefly.core.insurance.underwriting.interfaces.enums.SubjectivityStatus;

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
 * Condition that must be cleared before bind.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectivityDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID subjectivityId;

    @FilterableId
    @NotNull(message = "Submission id is required")
    private UUID submissionId;

    @FilterableId
    @NotNull(message = "quoteId is required")
    private UUID quoteId;

    @NotBlank(message = "description is required")
    @Size(max = 4000, message = "description must not exceed 4000 characters")
    private String description;

    @NotNull(message = "subjectivityStatus is required")
    private SubjectivityStatus subjectivityStatus;

    private LocalDate dueDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
