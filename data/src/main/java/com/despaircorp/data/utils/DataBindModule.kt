package com.despaircorp.data.utils

import com.despaircorp.data.estate.EstateDataRepository
import com.despaircorp.data.geocoder.GeocoderDataRepository
import com.despaircorp.data.picture.PictureDataRepository
import com.despaircorp.data.real_estate_agent.RealEstateAgentDataRepository
import com.despaircorp.domain.estate.EstateDomainRepository
import com.despaircorp.domain.geocoder.GeocoderDomainRepository
import com.despaircorp.domain.picture.PictureDomainRepository
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
    
    @Binds
    @Singleton
    abstract fun bindEstateRepository(impl: EstateDataRepository): EstateDomainRepository
    
    @Binds
    @Singleton
    abstract fun bindPictureRepository(impl: PictureDataRepository): PictureDomainRepository
    
    @Binds
    @Singleton
    abstract fun bindGeocoderRepository(impl: GeocoderDataRepository): GeocoderDomainRepository
    
    
}
