package com.despaircorp.data.utils

import android.app.Application
import android.content.res.Resources
import androidx.work.WorkManager
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
    fun provideEntitiesMaperinator(): EntitiesMaperinator = EntitiesMaperinator
    
    @Provides
    @Singleton
    fun provideWorkManager(application: Application): WorkManager {
        return WorkManager.getInstance(application)
    }
}