package com.despaircorp.data.utils

import android.app.Application
import android.content.res.AssetManager
import android.content.res.Resources
import android.location.Geocoder
import androidx.work.WorkManager
import com.despaircorp.data.estate.dao.EstateDao
import com.despaircorp.data.picture.dao.PictureDao
import com.despaircorp.data.real_estate_agent.dao.RealEstateAgentDao
import com.despaircorp.data.room_database.RealEstateManagerRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataProvideModule {
    @Provides
    @Singleton
    fun provideResources(application: Application): Resources {
        return application.resources
    }
    
    @Provides
    @Singleton
    fun provideRealEstateManagerRoomDatabase(application: Application): RealEstateManagerRoomDatabase =
        RealEstateManagerRoomDatabase.getDatabase(application.applicationContext)
    
    @Provides
    @Singleton
    fun provideRealEstateAgentDao(application: Application): RealEstateAgentDao =
        RealEstateManagerRoomDatabase.getDatabase(application.applicationContext)
            .getRealEstateAgentDao()
    
    @Provides
    @Singleton
    fun provideEstateDao(application: Application): EstateDao =
        RealEstateManagerRoomDatabase.getDatabase(application.applicationContext)
            .getEstateDao()
    
    @Provides
    @Singleton
    fun providePictureDao(application: Application): PictureDao =
        RealEstateManagerRoomDatabase.getDatabase(application.applicationContext)
            .getPictureDao()
    
    @Provides
    @Singleton
    fun provideEntitiesMaperinator(): EntitiesMaperinator = EntitiesMaperinator
    
    @Provides
    @Singleton
    fun provideWorkManager(application: Application): WorkManager {
        return WorkManager.getInstance(application)
    }
    
    @Provides
    @Singleton
    fun provideAssetManager(application: Application): AssetManager {
        return application.assets
    }
    
    @Provides
    @Singleton
    fun provideGeocoder(application: Application): Geocoder {
        return Geocoder(application)
    }
}