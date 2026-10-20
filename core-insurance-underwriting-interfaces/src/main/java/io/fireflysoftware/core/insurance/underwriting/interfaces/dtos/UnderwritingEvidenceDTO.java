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

import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.EvidenceStatus;
import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.EvidenceType;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fireflyframework.utils.annotations.FilterableId;

/**
 * Evidence reference on a submission. The document bytes stay in document management..
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnderwritingEvidenceDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID underwritingEvidenceId;

    @FilterableId
    @NotNull(message = "Submission id is required")
    private UUID submissionId;

    /** Logical reference: core-common-document-mgmt document. This service does not own that record. */
    @FilterableId
    @NotNull(message = "documentId is required")
    private UUID documentId;

    @NotNull(message = "evidenceType is required")
    private EvidenceType evidenceType;

    @NotNull(message = "evidenceStatus is required")
    private EvidenceStatus evidenceStatus;

    private LocalDateTime receivedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
