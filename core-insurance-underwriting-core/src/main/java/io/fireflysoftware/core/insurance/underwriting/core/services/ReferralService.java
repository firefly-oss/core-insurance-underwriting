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


package io.fireflysoftware.core.insurance.underwriting.core.services;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.insurance.underwriting.interfaces.dtos.ReferralDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for referral of a submission to a person with delegated authority.
 */
public interface ReferralService {

    /**
     * Filters referrals with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of ReferralDTO
     */
    Mono<PaginationResponse<ReferralDTO>> filterReferrals(UUID submissionId, FilterRequest<ReferralDTO> filterRequest);

    /**
     * Creates a Referral.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<ReferralDTO> createReferral(UUID submissionId, ReferralDTO dto);

    /**
     * Updates a Referral.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<ReferralDTO> updateReferral(UUID submissionId, UUID referralId, ReferralDTO dto);

    /**
     * Deletes a Referral.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteReferral(UUID submissionId, UUID referralId);

    /**
     * Loads a Referral by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<ReferralDTO> getReferralById(UUID submissionId, UUID referralId);
}
