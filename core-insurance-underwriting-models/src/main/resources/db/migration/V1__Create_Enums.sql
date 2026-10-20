-- Flyway V1: enum types for core-insurance-underwriting

CREATE TYPE submission_status_enum AS ENUM (
    'RECEIVED',
    'IN_CLEARANCE',
    'REFERRED',
    'QUOTED',
    'BOUND',
    'DECLINED',
    'WITHDRAWN'
);

CREATE TYPE line_of_business_enum AS ENUM (
    'MOTOR',
    'PROPERTY',
    'LIABILITY',
    'LIFE',
    'HEALTH',
    'MARINE',
    'AVIATION',
    'ACCIDENT',
    'OTHER'
);

CREATE TYPE insured_object_type_enum AS ENUM (
    'PERSON',
    'VEHICLE',
    'PROPERTY',
    'VESSEL',
    'AIRCRAFT',
    'LIABILITY',
    'OTHER'
);

CREATE TYPE evidence_type_enum AS ENUM (
    'APPLICATION',
    'STATEMENT_OF_VALUES',
    'LOSS_RUN',
    'SURVEY',
    'FINANCIALS',
    'OTHER'
);

CREATE TYPE evidence_status_enum AS ENUM (
    'REQUESTED',
    'RECEIVED',
    'ACCEPTED',
    'REJECTED'
);

CREATE TYPE referral_status_enum AS ENUM (
    'OPEN',
    'RETURNED',
    'APPROVED',
    'DECLINED'
);

CREATE TYPE decision_outcome_enum AS ENUM (
    'ACCEPT',
    'ACCEPT_WITH_CONDITIONS',
    'DECLINE',
    'REFER'
);

CREATE TYPE clearance_status_enum AS ENUM (
    'CLEARED',
    'CONFLICT',
    'PENDING'
);

CREATE TYPE screening_status_enum AS ENUM (
    'CLEAR',
    'POTENTIAL_MATCH',
    'CONFIRMED_MATCH',
    'NOT_SCREENED'
);

CREATE TYPE quote_status_enum AS ENUM (
    'DRAFT',
    'PRESENTED',
    'ACCEPTED',
    'EXPIRED',
    'DECLINED'
);

CREATE TYPE subjectivity_status_enum AS ENUM (
    'OPEN',
    'SATISFIED',
    'WAIVED'
);

