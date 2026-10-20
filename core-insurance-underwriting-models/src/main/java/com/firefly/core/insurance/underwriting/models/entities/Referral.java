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

import com.firefly.core.insurance.underwriting.interfaces.enums.ReferralStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Referral of a submission to a person with delegated authority.
 * Maps to the 'referral' table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("referral")
public class Referral {

    @Id
    @Column("referral_id")
    private UUID referralId;

    @Column("submission_id")
    private UUID submissionId;

    @Column("reason")
    private String reason;

    @Column("authority_level")
    private String authorityLevel;

    @Column("referral_status")
    private ReferralStatus referralStatus;

    @Column("referred_at")
    private LocalDateTime referredAt;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
