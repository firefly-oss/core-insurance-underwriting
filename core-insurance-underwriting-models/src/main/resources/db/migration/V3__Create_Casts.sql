-- Flyway V3: implicit varchar casts for enum parameters

CREATE CAST (varchar AS submission_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS line_of_business_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS insured_object_type_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS evidence_type_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS evidence_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS referral_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS decision_outcome_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS clearance_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS screening_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS quote_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS subjectivity_status_enum) WITH INOUT AS IMPLICIT;
