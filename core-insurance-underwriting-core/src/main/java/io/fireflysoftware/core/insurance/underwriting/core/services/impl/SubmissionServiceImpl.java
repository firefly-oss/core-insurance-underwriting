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

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.web.error.exceptions.ResourceNotFoundException;
import io.fireflysoftware.core.insurance.underwriting.core.mappers.SubmissionMapper;
import io.fireflysoftware.core.insurance.underwriting.core.services.SubmissionService;
import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.SubmissionDTO;
import io.fireflysoftware.core.insurance.underwriting.models.entities.Submission;
import io.fireflysoftware.core.insurance.underwriting.models.repositories.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SubmissionRepository repository;

    @Autowired
    private SubmissionMapper mapper;

    @Override
    public Mono<PaginationResponse<SubmissionDTO>> filterSubmissions(FilterRequest<SubmissionDTO> filterRequest) {
        return FilterUtils.createFilter(Submission.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<SubmissionDTO> createSubmission(SubmissionDTO dto) {
        return Mono.just(dto)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<SubmissionDTO> updateSubmission(UUID submissionId, SubmissionDTO dto) {
        return repository.findById(submissionId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Submission", submissionId.toString())))
                .flatMap(existing -> {
                    mapper.updateEntityFromDto(dto, existing);
                    existing.setSubmissionId(submissionId);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteSubmission(UUID submissionId) {
        return repository.findById(submissionId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Submission", submissionId.toString())))
                .flatMap(existing -> repository.deleteById(submissionId));
    }

    @Override
    public Mono<SubmissionDTO> getSubmissionById(UUID submissionId) {
        return repository.findById(submissionId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("Submission", submissionId.toString())))
                .map(mapper::toDTO);
    }
}
