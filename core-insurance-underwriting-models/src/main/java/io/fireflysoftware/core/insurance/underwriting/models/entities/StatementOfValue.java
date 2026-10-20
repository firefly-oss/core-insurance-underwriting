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


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * One normalized statement-of-values line on a submission.
 * Maps to the 'statement_of_value' table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("statement_of_value")
public class StatementOfValue {

    @Id
    @Column("statement_of_value_id")
    private UUID statementOfValueId;

    @Column("submission_id")
    private UUID submissionId;

    @Column("line_number")
    private Integer lineNumber;

    @Column("location_reference")
    private String locationReference;

    @Column("occupancy")
    private String occupancy;

    @Column("declared_value")
    private BigDecimal declaredValue;

    @Column("currency")
    private String currency;

    @Column("source_reference")
    private String sourceReference;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
