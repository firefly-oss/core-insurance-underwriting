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


package io.fireflysoftware.core.insurance.underwriting.core.services.impl;

import org.fireflyframework.web.error.exceptions.ResourceNotFoundException;
import io.fireflysoftware.core.insurance.underwriting.core.mappers.SanctionsScreenMapper;
import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.SanctionsScreenDTO;
import io.fireflysoftware.core.insurance.underwriting.models.entities.SanctionsScreen;
import io.fireflysoftware.core.insurance.underwriting.models.repositories.SanctionsScreenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SanctionsScreenServiceImplTest {

    @Mock
    private SanctionsScreenRepository repository;

    @Mock
    private SanctionsScreenMapper mapper;

    @InjectMocks
    private SanctionsScreenServiceImpl service;

    private SanctionsScreenDTO dto;
    private SanctionsScreen entity;
    private UUID sanctionsScreenId;
    private UUID submissionId;

    @BeforeEach
    void setUp() {
        sanctionsScreenId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        entity = new SanctionsScreen();
        entity.setSanctionsScreenId(sanctionsScreenId);
        entity.setCreatedAt(LocalDateTime.parse("2026-01-15T10:15:30"));
        entity.setUpdatedAt(LocalDateTime.parse("2026-01-15T10:15:30"));
        dto = new SanctionsScreenDTO();
        dto.setSanctionsScreenId(sanctionsScreenId);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        submissionId = UUID.fromString("223e4567-e89b-12d3-a456-426614174000");
        entity.setSubmissionId(submissionId);
        dto.setSubmissionId(submissionId);
    }

    @Test
    void create_ShouldReturnDto_WhenSaveSucceeds() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        StepVerifier.create(service.createSanctionsScreen(submissionId, dto))
                .expectNext(dto)
                .verifyComplete();

        verify(repository).save(entity);
    }

    @Test
    void getById_ShouldReturnDto_WhenFound() {
        when(repository.findById(sanctionsScreenId)).thenReturn(Mono.just(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        StepVerifier.create(service.getSanctionsScreenById(submissionId, sanctionsScreenId))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void getById_ShouldError_WhenMissing() {
        when(repository.findById(sanctionsScreenId)).thenReturn(Mono.empty());

        StepVerifier.create(service.getSanctionsScreenById(submissionId, sanctionsScreenId))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void update_ShouldSaveExistingRow() {
        when(repository.findById(sanctionsScreenId)).thenReturn(Mono.just(entity));
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        StepVerifier.create(service.updateSanctionsScreen(submissionId, sanctionsScreenId, dto))
                .expectNext(dto)
                .verifyComplete();

        verify(mapper).updateEntityFromDto(dto, entity);
    }

    @Test
    void delete_ShouldDelete_WhenFound() {
        when(repository.findById(sanctionsScreenId)).thenReturn(Mono.just(entity));
        when(repository.deleteById(sanctionsScreenId)).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteSanctionsScreen(submissionId, sanctionsScreenId))
                .verifyComplete();
    }

    @Test
    void getById_ShouldError_WhenParentDoesNotMatch() {
        entity.setSubmissionId(UUID.fromString("323e4567-e89b-12d3-a456-426614174000"));
        when(repository.findById(sanctionsScreenId)).thenReturn(Mono.just(entity));

        StepVerifier.create(service.getSanctionsScreenById(submissionId, sanctionsScreenId))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
}
