package com.despaircorp.stubs

import com.despaircorp.data.real_estate_agent.dto.RealEstateAgentDto
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity

object EntityProvidinator {
    private const val DEFAULT_NAME = "DEFAULT_NAME"
    private const val DEFAULT_LOGGED_IN_FALSE = false
    private const val DEFAULT_ID = 1
    private const val DEFAULT_IMAGE_RESOURCE = 1
    private const val DEFAULT_LOGGED_IN_TRUE = true
    
    
    fun provideRealEstateAgentEntities() = List(3) {
        RealEstateAgentEntity(
            name = DEFAULT_NAME + "$it",
            id = it,
            imageResource = it,
            isLoggedIn = DEFAULT_LOGGED_IN_FALSE
        )
    }
    
    fun provideRealEstateDtoEntities() = List(3) {
        RealEstateAgentDto(
            name = DEFAULT_NAME + "$it",
            id = it,
            imageResource = it,
            isLoggedIn = DEFAULT_LOGGED_IN_FALSE
        )
    }
    
    
    fun provideLoggedRealEstateAgentDto() = RealEstateAgentDto(
        id = DEFAULT_ID,
        name = DEFAULT_NAME,
        imageResource = DEFAULT_IMAGE_RESOURCE,
        isLoggedIn = DEFAULT_LOGGED_IN_TRUE
    )
    
    fun provideLoggedRealEstateAgentEntity() = RealEstateAgentEntity(
        id = DEFAULT_ID,
        name = DEFAULT_NAME,
        imageResource = DEFAULT_IMAGE_RESOURCE,
        isLoggedIn = DEFAULT_LOGGED_IN_TRUE
    )
}