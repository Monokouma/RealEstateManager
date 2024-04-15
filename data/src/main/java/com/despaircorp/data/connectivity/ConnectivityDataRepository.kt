package com.despaircorp.data.connectivity

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.despaircorp.data.utils.CoroutineDispatcherProvider
import com.despaircorp.domain.connectivity.ConnectivityDomainRepository
import com.despaircorp.domain.connectivity.NetworkType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ConnectivityDataRepository @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val connectivityManager: ConnectivityManager
) : ConnectivityDomainRepository {
    
    override fun isConnectedToInternet(): Flow<NetworkType> = callbackFlow {
        trySend(connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)?.let { networkCapabilities ->
                when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
                    else -> NetworkType.NONE
                }
            } ?: NetworkType.NONE
        } ?: NetworkType.NONE)
        
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                val networkType = when {
                    networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> NetworkType.WIFI
                    networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> NetworkType.CELLULAR
                    else -> NetworkType.NONE
                }
                trySend(networkType)
            }
            
            override fun onLost(network: Network) {
                trySend(NetworkType.NONE)
            }
        }
        
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        
        awaitClose { connectivityManager.unregisterNetworkCallback(networkCallback) }
        
    }.distinctUntilChanged().flowOn(coroutineDispatcherProvider.io)
    
}