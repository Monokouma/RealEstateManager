package com.despaircorp.domain.geocoder

import android.location.Address
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator
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

class GetAddressFromLatLngUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val geocoderDomainRepository: GeocoderDomainRepository = mockk()
    
    private val mockedAddress: Address = mockk() {
        coEvery { getAddressLine(0) } returns EntityProvidinator.DEFAULT_ESTATE_ADDRESS
        coEvery { locality } returns EntityProvidinator.DEFAULT_ESTATE_CITY
    }
    
    private val useCase = GetAddressFromLatLngUseCase(
        geocoderDomainRepository
    )
    
    @Before
    fun setup() {
        coEvery { geocoderDomainRepository.resolveAddressFromLatLng(DEFAULT_ESTATE_LOCATION) } returns mutableListOf(
            mockedAddress
        )
    }
    
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        val result = useCase.invoke(DEFAULT_ESTATE_LOCATION)
        
        assertThat(result).isEqualTo(
            mapOf(
                "city" to DEFAULT_ESTATE_CITY,
                "address" to DEFAULT_ESTATE_ADDRESS
            )
        )
        
        coVerify {
            geocoderDomainRepository.resolveAddressFromLatLng(DEFAULT_ESTATE_LOCATION)
        }
        
        confirmVerified(geocoderDomainRepository)
    }
}