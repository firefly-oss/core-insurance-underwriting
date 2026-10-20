-- Flyway V2: tables for core-insurance-underwriting

-- Underwriting submission received for a product and applicant
CREATE TABLE submission (
    submission_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    submission_number VARCHAR(64) NOT NULL UNIQUE,
    party_id UUID NOT NULL,
    product_id UUID NOT NULL,
    distributor_id UUID NULL,
    contract_id UUID NULL,
    submission_status submission_status_enum NOT NULL,
    line_of_business line_of_business_enum NOT NULL,
    received_at TIMESTAMP WITH TIME ZONE NOT NULL,
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Risk item on a submission
CREATE TABLE submission_risk (
    submission_risk_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID NOT NULL REFERENCES submission (submission_id) ON DELETE CASCADE,
    risk_number VARCHAR(64) NOT NULL,
    description TEXT NULL,
    insured_object_type insured_object_type_enum NOT NULL,
    party_id UUID NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Evidence reference on a submission. The document bytes stay in document management.
CREATE TABLE underwriting_evidence (
    underwriting_evidence_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID NOT NULL REFERENCES submission (submission_id) ON DELETE CASCADE,
    document_id UUID NOT NULL,
    evidence_type evidence_type_enum NOT NULL,
    evidence_status evidence_status_enum NOT NULL,
    received_at TIMESTAMP WITH TIME ZONE NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Referral of a submission to a person with delegated authority
CREATE TABLE referral (
    referral_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID NOT NULL REFERENCES submission (submission_id) ON DELETE CASCADE,
    reason TEXT NOT NULL,
    authority_level VARCHAR(100) NULL,
    referral_status referral_status_enum NOT NULL,
    referred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Recorded underwriting decision. Authority stays with the insurer.
CREATE TABLE underwriting_decision (
    underwriting_decision_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID NOT NULL REFERENCES submission (submission_id) ON DELETE CASCADE,
    decision_outcome decision_outcome_enum NOT NULL,
    conditions TEXT NULL,
    decided_at TIMESTAMP WITH TIME ZONE NOT NULL,
    authority_reference VARCHAR(200) NULL,
    bound_policy_id UUID NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Status change recorded against a submission
CREATE TABLE submission_status_history (
    submission_status_history_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID NOT NULL REFERENCES submission (submission_id) ON DELETE CASCADE,
    submission_status submission_status_enum NOT NULL,
    status_reason VARCHAR(500) NULL,
    changed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Clearance of a submission against in-force business and the broker of record
CREATE TABLE clearance (
    clearance_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID NOT NULL REFERENCES submission (submission_id) ON DELETE CASCADE,
    clearance_status clearance_status_enum NOT NULL,
    in_force_policy_id UUID NULL,
    broker_of_record_party_id UUID NULL,
    cleared_at TIMESTAMP WITH TIME ZONE NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Recorded sanctions screening result. The check itself stays with the provider.
CREATE TABLE sanctions_screen (
    sanctions_screen_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID NOT NULL REFERENCES submission (submission_id) ON DELETE CASCADE,
    party_id UUID NOT NULL,
    screening_status screening_status_enum NOT NULL,
    provider_reference VARCHAR(128) NULL,
    screened_at TIMESTAMP WITH TIME ZONE NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- One normalized statement-of-values line on a submission
CREATE TABLE statement_of_value (
    statement_of_value_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID NOT NULL REFERENCES submission (submission_id) ON DELETE CASCADE,
    line_number INTEGER NOT NULL,
    location_reference VARCHAR(128) NULL,
    occupancy VARCHAR(128) NULL,
    declared_value DECIMAL(19, 4) NULL,
    currency VARCHAR(3) NULL,
    source_reference VARCHAR(128) NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Prior-year loss run summarized by line and cause
CREATE TABLE loss_run (
    loss_run_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID NOT NULL REFERENCES submission (submission_id) ON DELETE CASCADE,
    prior_carrier_name VARCHAR(255) NULL,
    line_of_business line_of_business_enum NULL,
    loss_year INTEGER NOT NULL,
    cause_code VARCHAR(64) NULL,
    claim_count INTEGER NULL,
    incurred_amount DECIMAL(19, 4) NULL,
    currency VARCHAR(3) NULL,
    large_loss BOOLEAN NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Quote prepared from authorized terms. Rating stays outside this service.
CREATE TABLE quote (
    quote_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID NOT NULL REFERENCES submission (submission_id) ON DELETE CASCADE,
    quote_number VARCHAR(64) NOT NULL,
    quote_status quote_status_enum NOT NULL,
    premium_amount DECIMAL(19, 4) NULL,
    currency VARCHAR(3) NOT NULL,
    valid_until DATE NULL,
    bound_policy_id UUID NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Condition that must be cleared before bind
CREATE TABLE subjectivity (
    subjectivity_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID NOT NULL REFERENCES submission (submission_id) ON DELETE CASCADE,
    quote_id UUID NOT NULL REFERENCES quote (quote_id) ON DELETE CASCADE,
    description TEXT NOT NULL,
    subjectivity_status subjectivity_status_enum NOT NULL,
    due_date DATE NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_submission_tenant_id ON submission (tenant_id);
CREATE INDEX idx_submission_party_id ON submission (party_id);
CREATE INDEX idx_submission_product_id ON submission (product_id);
CREATE INDEX idx_submission_distributor_id ON submission (distributor_id);
CREATE INDEX idx_submission_contract_id ON submission (contract_id);
CREATE INDEX idx_submission_risk_submission_id ON submission_risk (submission_id);
CREATE INDEX idx_submission_risk_party_id ON submission_risk (party_id);
CREATE INDEX idx_underwriting_evidence_submission_id ON underwriting_evidence (submission_id);
CREATE INDEX idx_underwriting_evidence_document_id ON underwriting_evidence (document_id);
CREATE INDEX idx_referral_submission_id ON referral (submission_id);
CREATE INDEX idx_underwriting_decision_submission_id ON underwriting_decision (submission_id);
CREATE INDEX idx_underwriting_decision_bound_policy_id ON underwriting_decision (bound_policy_id);
CREATE INDEX idx_submission_status_history_submission_id ON submission_status_history (submission_id);
CREATE INDEX idx_clearance_submission_id ON clearance (submission_id);
CREATE INDEX idx_clearance_in_force_policy_id ON clearance (in_force_policy_id);
CREATE INDEX idx_clearance_broker_of_record_party_id ON clearance (broker_of_record_party_id);
CREATE INDEX idx_sanctions_screen_submission_id ON sanctions_screen (submission_id);
CREATE INDEX idx_sanctions_screen_party_id ON sanctions_screen (party_id);
CREATE INDEX idx_statement_of_value_submission_id ON statement_of_value (submission_id);
CREATE INDEX idx_loss_run_submission_id ON loss_run (submission_id);
CREATE INDEX idx_quote_submission_id ON quote (submission_id);
CREATE INDEX idx_quote_bound_policy_id ON quote (bound_policy_id);
CREATE INDEX idx_subjectivity_submission_id ON subjectivity (submission_id);
CREATE INDEX idx_subjectivity_quote_id ON subjectivity (quote_id);
COMMENT ON COLUMN submission.tenant_id IS 'Logical reference: Firefly deployment tenant. No foreign key across services.';
COMMENT ON COLUMN submission.party_id IS 'Logical reference: core-common-customer-mgmt party. No foreign key across services.';
COMMENT ON COLUMN submission.product_id IS 'Logical reference: core-common-product-mgmt product. No foreign key across services.';
COMMENT ON COLUMN submission.distributor_id IS 'Logical reference: core-common-distributor-mgmt distributor. No foreign key across services.';
COMMENT ON COLUMN submission.contract_id IS 'Logical reference: core-common-contract-mgmt contract. No foreign key across services.';
COMMENT ON COLUMN submission_risk.party_id IS 'Logical reference: core-common-customer-mgmt party. No foreign key across services.';
COMMENT ON COLUMN underwriting_evidence.document_id IS 'Logical reference: core-common-document-mgmt document. No foreign key across services.';
COMMENT ON COLUMN underwriting_decision.bound_policy_id IS 'Logical reference: core-insurance-policies policy. No foreign key across services.';
COMMENT ON COLUMN clearance.in_force_policy_id IS 'Logical reference: core-insurance-policies policy. No foreign key across services.';
COMMENT ON COLUMN clearance.broker_of_record_party_id IS 'Logical reference: core-common-customer-mgmt party. No foreign key across services.';
COMMENT ON COLUMN sanctions_screen.party_id IS 'Logical reference: core-common-customer-mgmt party. No foreign key across services.';
COMMENT ON COLUMN quote.bound_policy_id IS 'Logical reference: core-insurance-policies policy. No foreign key across services.';
