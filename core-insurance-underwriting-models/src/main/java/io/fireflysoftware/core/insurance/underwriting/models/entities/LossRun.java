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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.LineOfBusiness;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Prior-year loss run summarized by line and cause.
 * Maps to the 'loss_run' table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("loss_run")
public class LossRun {

    @Id
    @Column("loss_run_id")
    private UUID lossRunId;

    @Column("submission_id")
    private UUID submissionId;

    @Column("prior_carrier_name")
    private String priorCarrierName;

    @Column("line_of_business")
    private LineOfBusiness lineOfBusiness;

    @Column("loss_year")
    private Integer lossYear;

    @Column("cause_code")
    private String causeCode;

    @Column("claim_count")
    private Integer claimCount;

    @Column("incurred_amount")
    private BigDecimal incurredAmount;

    @Column("currency")
    private String currency;

    @Column("large_loss")
    private Boolean largeLoss;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
