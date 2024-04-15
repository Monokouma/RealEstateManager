package com.despaircorp.data.connectivity

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.domain.connectivity.NetworkType
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runCurrent
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ConnectivityDataRepositoryUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val connectivityManager: ConnectivityManager = mockk()
    private val networkMock: Network = mockk()
    private val networkCapabilitiesMockk: NetworkCapabilities = mockk()
    private lateinit var repository: ConnectivityDataRepository
    
    @Before
    fun setup() {
        every { connectivityManager.activeNetwork } returns networkMock
        every { connectivityManager.getNetworkCapabilities(networkMock) } returns networkCapabilitiesMockk
        every { networkCapabilitiesMockk.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        
        repository = ConnectivityDataRepository(
            coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
            connectivityManager = connectivityManager
        )
    }
    
    @Test
    fun `nominal case - function should return TRANSPORT_WIFI`() = testCoroutineRule.runTest {
        val slot = slot<NetworkCallback>()
        
        every { connectivityManager.registerDefaultNetworkCallback(capture(slot)) } returns mockk()
        
        repository.isConnectedToInternet().test {
            runCurrent()
            slot.captured.onAvailable(networkMock)
            cancel()
            val result = awaitItem()
            awaitComplete()
            
            assertThat(result).isEqualTo(NetworkType.WIFI)
            
            coVerify {
                connectivityManager.activeNetwork
                connectivityManager.getNetworkCapabilities(networkMock)
                connectivityManager.unregisterNetworkCallback(capture(slot))
                connectivityManager.registerDefaultNetworkCallback(capture(slot))
            }
            
            confirmVerified(connectivityManager)
        }
    }
    
    @Test
    fun `nominal case - function should return TRANSPORT_CELLULAR`() = testCoroutineRule.runTest {
        val slot = slot<NetworkCallback>()
        every { networkCapabilitiesMockk.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { networkCapabilitiesMockk.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
        every { connectivityManager.registerDefaultNetworkCallback(capture(slot)) } returns mockk()
        
        repository.isConnectedToInternet().test {
            runCurrent()
            slot.captured.onAvailable(networkMock)
            cancel()
            val result = awaitItem()
            awaitComplete()
            
            assertThat(result).isEqualTo(NetworkType.CELLULAR)
            
            coVerify {
                connectivityManager.activeNetwork
                connectivityManager.getNetworkCapabilities(networkMock)
                connectivityManager.unregisterNetworkCallback(capture(slot))
                connectivityManager.registerDefaultNetworkCallback(capture(slot))
            }
            
            confirmVerified(connectivityManager)
        }
    }
    
    @Test
    fun `nominal case - function should return NONE`() = testCoroutineRule.runTest {
        val slot = slot<NetworkCallback>()
        every { networkCapabilitiesMockk.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { networkCapabilitiesMockk.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
        
        every { connectivityManager.registerDefaultNetworkCallback(capture(slot)) } returns mockk()
        
        repository.isConnectedToInternet().test {
            runCurrent()
            slot.captured.onAvailable(networkMock)
            cancel()
            val result = awaitItem()
            awaitComplete()
            
            assertThat(result).isEqualTo(NetworkType.NONE)
            
            coVerify {
                connectivityManager.activeNetwork
                connectivityManager.getNetworkCapabilities(networkMock)
                connectivityManager.unregisterNetworkCallback(capture(slot))
                connectivityManager.registerDefaultNetworkCallback(capture(slot))
            }
            
            confirmVerified(connectivityManager)
        }
    }
    
    @Test
    fun `nominal case - function should return NONE cause null active network`() =
        testCoroutineRule.runTest {
            val slot = slot<NetworkCallback>()
            every { connectivityManager.activeNetwork } returns null
            
            every { connectivityManager.registerDefaultNetworkCallback(capture(slot)) } returns mockk()
            
            repository.isConnectedToInternet().test {
                runCurrent()
                slot.captured.onAvailable(networkMock)
                cancel()
                val result = awaitItem()
                awaitComplete()
                
                assertThat(result).isEqualTo(NetworkType.NONE)
                
                coVerify {
                    connectivityManager.activeNetwork
                    connectivityManager.getNetworkCapabilities(networkMock)
                    connectivityManager.unregisterNetworkCallback(capture(slot))
                    connectivityManager.registerDefaultNetworkCallback(capture(slot))
                }
                
                confirmVerified(connectivityManager)
            }
        }
    
    @Test
    fun `nominal case - function should return NONE cause null capabilities`() =
        testCoroutineRule.runTest {
            val slot = slot<NetworkCallback>()
            every { connectivityManager.getNetworkCapabilities(networkMock) } returns null
            every { connectivityManager.registerDefaultNetworkCallback(capture(slot)) } returns mockk()
            
            repository.isConnectedToInternet().test {
                runCurrent()
                slot.captured.onAvailable(networkMock)
                cancel()
                val result = awaitItem()
                awaitComplete()
                
                assertThat(result).isEqualTo(NetworkType.NONE)
                
                coVerify {
                    connectivityManager.activeNetwork
                    connectivityManager.getNetworkCapabilities(networkMock)
                    connectivityManager.unregisterNetworkCallback(capture(slot))
                    connectivityManager.registerDefaultNetworkCallback(capture(slot))
                }
                
                confirmVerified(connectivityManager)
            }
        }
    
    @Test
    fun `nominal case - function should return NONE on lost`() =
        testCoroutineRule.runTest {
            val slot = slot<NetworkCallback>()
            every { connectivityManager.getNetworkCapabilities(networkMock) } returns null
            every { connectivityManager.registerDefaultNetworkCallback(capture(slot)) } returns mockk()
            
            repository.isConnectedToInternet().test {
                runCurrent()
                slot.captured.onLost(networkMock)
                cancel()
                val result = awaitItem()
                awaitComplete()
                
                assertThat(result).isEqualTo(NetworkType.NONE)
                
                coVerify {
                    connectivityManager.activeNetwork
                    connectivityManager.getNetworkCapabilities(networkMock)
                    connectivityManager.unregisterNetworkCallback(capture(slot))
                    connectivityManager.registerDefaultNetworkCallback(capture(slot))
                }
                
                confirmVerified(connectivityManager)
            }
        }
}