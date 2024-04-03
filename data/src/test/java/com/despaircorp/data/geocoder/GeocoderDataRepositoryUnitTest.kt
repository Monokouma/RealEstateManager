package com.despaircorp.data.geocoder

import android.location.Address
import android.location.Geocoder
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.data.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_ADDRESS
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_CITY
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_LOCATION
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GeocoderDataRepositoryUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val geocoder: Geocoder = mockk()
    
    private lateinit var repository: GeocoderDataRepository
    
    private val mockedAddress: Address = mockk() {
        coEvery { getAddressLine(0) } returns DEFAULT_ESTATE_ADDRESS
        coEvery { locality } returns DEFAULT_ESTATE_CITY
    }
    
    @Before
    fun setup() {
        coEvery {
            geocoder.getFromLocation(
                DEFAULT_ESTATE_LOCATION.latitude,
                DEFAULT_ESTATE_LOCATION.longitude,
                1
            )
        } returns mutableListOf(mockedAddress)
        
        repository = GeocoderDataRepository(
            testCoroutineRule.getTestCoroutineDispatcherProvider(),
            geocoder
        )
    }
    
    @Test
    fun `error case - get address from lat lng return empty list`() = testCoroutineRule.runTest {
        coEvery {
            geocoder.getFromLocation(
                DEFAULT_ESTATE_LOCATION.latitude,
                DEFAULT_ESTATE_LOCATION.longitude,
                1
            )
        } returns null
        
        val result = repository.resolveAddressFromLatLng(DEFAULT_ESTATE_LOCATION)
        
        assertThat(
            result
        ).isEqualTo(emptyList())
        
        coVerify {
            geocoder.getFromLocation(
                DEFAULT_ESTATE_LOCATION.latitude,
                DEFAULT_ESTATE_LOCATION.longitude,
                1
            )
        }
        
        confirmVerified(geocoder)
    }
    
    @Test
    fun `nominal case - get address from lat lng`() = testCoroutineRule.runTest {
        val result = repository.resolveAddressFromLatLng(DEFAULT_ESTATE_LOCATION)
        
        assertThat(
            result.first()
        ).isEqualTo(provideAddressList()?.first())
        
        coVerify {
            geocoder.getFromLocation(
                DEFAULT_ESTATE_LOCATION.latitude,
                DEFAULT_ESTATE_LOCATION.longitude,
                1
            )
        }
        
        confirmVerified(geocoder)
    }
    
    private fun provideAddressList() = geocoder.getFromLocation(
        DEFAULT_ESTATE_LOCATION.latitude,
        DEFAULT_ESTATE_LOCATION.longitude,
        1
    )
    
}