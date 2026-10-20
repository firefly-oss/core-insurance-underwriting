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


package io.fireflysoftware.core.insurance.underwriting.models.config;

import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.SubmissionStatus;
import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.LineOfBusiness;
import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.InsuredObjectType;
import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.EvidenceType;
import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.EvidenceStatus;
import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.ReferralStatus;
import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.DecisionOutcome;
import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.ClearanceStatus;
import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.ScreeningStatus;
import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.QuoteStatus;
import io.fireflysoftware.core.insurance.underwriting.interfaces.enums.SubjectivityStatus;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.client.SSLMode;
import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Registers every PostgreSQL enum used by this service with the R2DBC codec.
 * Without this, the driver binds enum parameters as varchar and Postgres rejects the cast.
 */
@Configuration
public class R2dbcConfiguration extends AbstractR2dbcConfiguration {

    @Value("${DB_HOST:localhost}")
    private String host;

    @Value("${DB_PORT:5432}")
    private int port;

    @Value("${DB_NAME:postgres}")
    private String database;

    @Value("${DB_USERNAME:postgres}")
    private String username;

    @Value("${DB_PASSWORD:postgres}")
    private String password;

    @Value("${DB_SSL_MODE:disable}")
    private String sslMode;

    @WritingConverter
    static class SubmissionStatusConverter implements Converter<SubmissionStatus, SubmissionStatus> {
        @Override
        public SubmissionStatus convert(SubmissionStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class LineOfBusinessConverter implements Converter<LineOfBusiness, LineOfBusiness> {
        @Override
        public LineOfBusiness convert(LineOfBusiness source) {
            return source;
        }
    }

    @WritingConverter
    static class InsuredObjectTypeConverter implements Converter<InsuredObjectType, InsuredObjectType> {
        @Override
        public InsuredObjectType convert(InsuredObjectType source) {
            return source;
        }
    }

    @WritingConverter
    static class EvidenceTypeConverter implements Converter<EvidenceType, EvidenceType> {
        @Override
        public EvidenceType convert(EvidenceType source) {
            return source;
        }
    }

    @WritingConverter
    static class EvidenceStatusConverter implements Converter<EvidenceStatus, EvidenceStatus> {
        @Override
        public EvidenceStatus convert(EvidenceStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class ReferralStatusConverter implements Converter<ReferralStatus, ReferralStatus> {
        @Override
        public ReferralStatus convert(ReferralStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class DecisionOutcomeConverter implements Converter<DecisionOutcome, DecisionOutcome> {
        @Override
        public DecisionOutcome convert(DecisionOutcome source) {
            return source;
        }
    }

    @WritingConverter
    static class ClearanceStatusConverter implements Converter<ClearanceStatus, ClearanceStatus> {
        @Override
        public ClearanceStatus convert(ClearanceStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class ScreeningStatusConverter implements Converter<ScreeningStatus, ScreeningStatus> {
        @Override
        public ScreeningStatus convert(ScreeningStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class QuoteStatusConverter implements Converter<QuoteStatus, QuoteStatus> {
        @Override
        public QuoteStatus convert(QuoteStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class SubjectivityStatusConverter implements Converter<SubjectivityStatus, SubjectivityStatus> {
        @Override
        public SubjectivityStatus convert(SubjectivityStatus source) {
            return source;
        }
    }

    @Override
    protected List<Object> getCustomConverters() {
        List<Object> converters = new ArrayList<>();
        converters.add(new SubmissionStatusConverter());
        converters.add(new LineOfBusinessConverter());
        converters.add(new InsuredObjectTypeConverter());
        converters.add(new EvidenceTypeConverter());
        converters.add(new EvidenceStatusConverter());
        converters.add(new ReferralStatusConverter());
        converters.add(new DecisionOutcomeConverter());
        converters.add(new ClearanceStatusConverter());
        converters.add(new ScreeningStatusConverter());
        converters.add(new QuoteStatusConverter());
        converters.add(new SubjectivityStatusConverter());
        return converters;
    }

    @Bean
    @Primary
    @Override
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(host)
                        .port(port)
                        .username(username)
                        .password(password)
                        .database(database)
                        .sslMode(SSLMode.valueOf(sslMode.toUpperCase()))
                        .codecRegistrar(EnumCodec.builder()
                                .withEnum("submission_status_enum", SubmissionStatus.class)
                                .withEnum("line_of_business_enum", LineOfBusiness.class)
                                .withEnum("insured_object_type_enum", InsuredObjectType.class)
                                .withEnum("evidence_type_enum", EvidenceType.class)
                                .withEnum("evidence_status_enum", EvidenceStatus.class)
                                .withEnum("referral_status_enum", ReferralStatus.class)
                                .withEnum("decision_outcome_enum", DecisionOutcome.class)
                                .withEnum("clearance_status_enum", ClearanceStatus.class)
                                .withEnum("screening_status_enum", ScreeningStatus.class)
                                .withEnum("quote_status_enum", QuoteStatus.class)
                                .withEnum("subjectivity_status_enum", SubjectivityStatus.class)
                                .build())
                        .build()
        );
    }
}
