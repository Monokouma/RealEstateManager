package com.despaircorp.domain.splash_screen

import com.despaircorp.domain.utils.CoroutineDispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class CountDownSplashScreenUseCase @Inject constructor(
    private val dispatcherProvider: CoroutineDispatcherProvider
) {
    suspend fun invoke(): Boolean = withContext(dispatcherProvider.io) {
        for (i in 2 downTo 0) {
            delay(1.seconds)
        }
        true
    }
}