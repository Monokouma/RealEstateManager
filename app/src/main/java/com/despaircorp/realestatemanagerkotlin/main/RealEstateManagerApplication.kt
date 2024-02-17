package com.despaircorp.realestatemanagerkotlin.main

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.despaircorp.domain.real_estate_agent.EnqueueRealEstateAgentInitWorkerUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RealEstateManagerApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    
    @Inject
    lateinit var enqueueRealEstateAgentInitWorkerUseCase: EnqueueRealEstateAgentInitWorkerUseCase
    
    override fun onCreate() {
        super.onCreate()
        enqueueRealEstateAgentInitWorkerUseCase.invoke()
    }
    
    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .setMinimumLoggingLevel(android.util.Log.DEBUG)
        .build()
}