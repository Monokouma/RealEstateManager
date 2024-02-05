package com.despaircorp.data.utils

import com.despaircorp.data.real_estate_agent.RealEstateAgentDataRepository
import com.despaircorp.domain.real_estate_agent.RealEstateAgentDomainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataBindModule {
    @Binds
    @Singleton
    abstract fun bindRealEstateAgentRepository(impl: RealEstateAgentDataRepository): RealEstateAgentDomainRepository
}
