package com.despaircorp.domain.real_estate_agent

import javax.inject.Inject

class IsAgentCurrentlyLoggedInUseCase @Inject constructor(
    private val realEstateAgentDomainRepository: RealEstateAgentDomainRepository
) {
    suspend fun invoke(): Boolean = realEstateAgentDomainRepository.isUserCurrentlyLogged()
}