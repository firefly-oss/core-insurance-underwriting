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


package io.fireflysoftware.core.insurance.underwriting.models.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.EvidenceStatus;
import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.EvidenceType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Evidence reference on a submission. The document bytes stay in document management..
 * Maps to the 'underwriting_evidence' table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("underwriting_evidence")
public class UnderwritingEvidence {

    @Id
    @Column("underwriting_evidence_id")
    private UUID underwritingEvidenceId;

    @Column("submission_id")
    private UUID submissionId;

    @Column("document_id")
    private UUID documentId;

    @Column("evidence_type")
    private EvidenceType evidenceType;

    @Column("evidence_status")
    private EvidenceStatus evidenceStatus;

    @Column("received_at")
    private LocalDateTime receivedAt;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
