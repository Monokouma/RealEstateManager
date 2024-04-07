package com.despaircorp.realestatemanagerkotlin.main

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.despaircorp.domain.currency.EnqueueCurrencyWorkerUseCase
import com.despaircorp.domain.estate.EnqueueEstateWorkerUseCase
import com.despaircorp.domain.picture.EnqueuePictureWorkerUseCase
import com.despaircorp.domain.real_estate_agent.EnqueueRealEstateAgentInitWorkerUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RealEstateManagerApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    
    @Inject
    lateinit var enqueueRealEstateAgentInitWorkerUseCase: EnqueueRealEstateAgentInitWorkerUseCase
    
    @Inject
    lateinit var enqueueEstateWorkerUseCase: EnqueueEstateWorkerUseCase
    
    @Inject
    lateinit var enqueuePictureWorkerUseCase: EnqueuePictureWorkerUseCase
    
    @Inject
    lateinit var enqueueCurrencyWorkerUseCase: EnqueueCurrencyWorkerUseCase
    
    override fun onCreate() {
        super.onCreate()
        enqueueRealEstateAgentInitWorkerUseCase.invoke()
        enqueueEstateWorkerUseCase.invoke()
        enqueuePictureWorkerUseCase.invoke()
        enqueueCurrencyWorkerUseCase.invoke()
    }
    
    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .setMinimumLoggingLevel(android.util.Log.DEBUG)
        .build()
}