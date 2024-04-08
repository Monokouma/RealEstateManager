package com.despaircorp.domain.connectivity

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class IsUserConnectedToInternetUseCase @Inject constructor(
    private val connectivityDomainRepository: ConnectivityDomainRepository
) {
    fun invoke(): Flow<Boolean> = connectivityDomainRepository.isConnectedToInternet().transform {
        emit(
            it == NetworkType.CELLULAR || it == NetworkType.WIFI
        
        )
    }
}