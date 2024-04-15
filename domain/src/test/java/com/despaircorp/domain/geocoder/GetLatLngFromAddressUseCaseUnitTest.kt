package com.despaircorp.domain.geocoder

import android.location.Address
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.despaircorp.domain.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetLatLngFromAddressUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val geocoderDomainRepository: GeocoderDomainRepository = mockk()
    
    private val useCase = GetLatLngFromAddressUseCase(
        geocoderDomainRepository
    )
    
    private val mockedAddress: Address = mockk() {
        coEvery { getAddressLine(0) } returns EntityProvidinator.DEFAULT_ESTATE_ADDRESS
        coEvery { locality } returns EntityProvidinator.DEFAULT_ESTATE_CITY
        coEvery { latitude } returns EntityProvidinator.DEFAULT_ESTATE_LOCATION.latitude
        coEvery { longitude } returns EntityProvidinator.DEFAULT_ESTATE_LOCATION.longitude
    }
    
    @Before
    fun setup() {
        coEvery { geocoderDomainRepository.resolveLatLngFromAddress(EntityProvidinator.DEFAULT_ESTATE_ADDRESS) } returns mutableListOf(
            mockedAddress
        )
    }
    
    @Test
    fun `nominal case - get lat lng`() = testCoroutineRule.runTest {
        val result = useCase.invoke(EntityProvidinator.DEFAULT_ESTATE_ADDRESS)
        
        assertThat(result).isEqualTo(EntityProvidinator.DEFAULT_ESTATE_LOCATION)
        
        coVerify {
            geocoderDomainRepository.resolveLatLngFromAddress(EntityProvidinator.DEFAULT_ESTATE_ADDRESS)
        }
        
        confirmVerified(geocoderDomainRepository)
    }
    
    @Test
    fun `nominal case - error getting lat lng`() = testCoroutineRule.runTest {
        coEvery { geocoderDomainRepository.resolveLatLngFromAddress(EntityProvidinator.DEFAULT_ESTATE_ADDRESS) } returns emptyList()
        
        val result = useCase.invoke(EntityProvidinator.DEFAULT_ESTATE_ADDRESS)
        
        assertThat(result).isNull()
        
        coVerify {
            geocoderDomainRepository.resolveLatLngFromAddress(EntityProvidinator.DEFAULT_ESTATE_ADDRESS)
        }
        
        confirmVerified(geocoderDomainRepository)
    }
}