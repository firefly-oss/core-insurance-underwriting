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

import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.LineOfBusiness;
import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.SubmissionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Underwriting submission received for a product and applicant.
 * Maps to the 'submission' table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("submission")
public class Submission {

    @Id
    @Column("submission_id")
    private UUID submissionId;

    @Column("tenant_id")
    private UUID tenantId;

    @Column("submission_number")
    private String submissionNumber;

    @Column("party_id")
    private UUID partyId;

    @Column("product_id")
    private UUID productId;

    @Column("distributor_id")
    private UUID distributorId;

    @Column("contract_id")
    private UUID contractId;

    @Column("submission_status")
    private SubmissionStatus submissionStatus;

    @Column("line_of_business")
    private LineOfBusiness lineOfBusiness;

    @Column("received_at")
    private LocalDateTime receivedAt;

    @Column("currency")
    private String currency;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
