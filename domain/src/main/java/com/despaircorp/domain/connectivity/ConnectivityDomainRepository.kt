package com.despaircorp.domain.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityDomainRepository {
    
    fun isConnectedToInternet(): Flow<NetworkType>
}