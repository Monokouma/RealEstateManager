package com.despaircorp.data.location

import android.location.Location
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_LATITUDE
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_LONGITUDE
import com.despaircorp.stubs.EntityProvidinator.provideLocationEntity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runCurrent
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LocationDataRepositoryUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val fusedLocationProviderClient: FusedLocationProviderClient = mockk()
    
    private lateinit var repository: LocationDataRepository
    
    
    @Before
    fun setup() {
        
        repository = LocationDataRepository(
            testCoroutineRule.getTestCoroutineDispatcherProvider(),
            fusedLocationProviderClient
        )
    }
    
    @Test
    fun `nominal case - get user location as flow`() = testCoroutineRule.runTest {
        val location = mockk<Location>()
        every { location.latitude } returns DEFAULT_LATITUDE
        every { location.longitude } returns DEFAULT_LONGITUDE
        
        val locationResult = mockk<LocationResult>()
        every { locationResult.lastLocation?.latitude } returns DEFAULT_LATITUDE
        every { locationResult.lastLocation?.longitude } returns DEFAULT_LONGITUDE
        
        val slot = slot<LocationCallback>()
        every {
            fusedLocationProviderClient.requestLocationUpdates(
                any(),
                any(),
                capture(slot)
            )
        } returns mockk()
        
        repository.getUserLocationAsFlow().test {
            runCurrent()
            slot.captured.onLocationResult(locationResult)
            runCurrent()
            val result = awaitItem()
            
            assertThat(result).isEqualTo(
                provideLocationEntity()
            )
            
            coVerify {
                fusedLocationProviderClient.requestLocationUpdates(
                    any(),
                    any(),
                    capture(slot)
                )
            }
            
            confirmVerified(fusedLocationProviderClient)
        }
    }
    
    
}