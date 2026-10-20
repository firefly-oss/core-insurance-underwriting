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


package com.firefly.core.insurance.underwriting.models.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.firefly.core.insurance.underwriting.interfaces.enums.ClearanceStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Clearance of a submission against in-force business and the broker of record.
 * Maps to the 'clearance' table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("clearance")
public class Clearance {

    @Id
    @Column("clearance_id")
    private UUID clearanceId;

    @Column("submission_id")
    private UUID submissionId;

    @Column("clearance_status")
    private ClearanceStatus clearanceStatus;

    @Column("in_force_policy_id")
    private UUID inForcePolicyId;

    @Column("broker_of_record_party_id")
    private UUID brokerOfRecordPartyId;

    @Column("cleared_at")
    private LocalDateTime clearedAt;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
