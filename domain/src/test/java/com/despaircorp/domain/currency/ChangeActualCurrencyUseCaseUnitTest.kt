package com.despaircorp.domain.currency

import com.despaircorp.domain.currency.model.CurrencyEntity
import com.despaircorp.domain.currency.model.CurrencyEnum
import com.despaircorp.domain.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChangeActualCurrencyUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val currencyDomainRepository: CurrencyDomainRepository = mockk()
    
    private val useCase = ChangeActualCurrencyUseCase(
        currencyDomainRepository
    )
    
    @Before
    fun setup() {
        coEvery { currencyDomainRepository.getActualCurrencyEntity() } returns EntityProvidinator.provideCurrencyEntity()
        coJustRun { currencyDomainRepository.insertCurrencyEntity(CurrencyEntity(CurrencyEnum.US_DOLLAR)) }
        coJustRun { currencyDomainRepository.insertCurrencyEntity(CurrencyEntity(CurrencyEnum.EURO)) }
    }
    
    @Test
    fun `nominal case - actual currency is dollar should trigger change to euro`() =
        testCoroutineRule.runTest {
            useCase.invoke()
            
            coVerify {
                currencyDomainRepository.getActualCurrencyEntity()
                currencyDomainRepository.insertCurrencyEntity(CurrencyEntity(CurrencyEnum.EURO))
            }
            
            confirmVerified(
                currencyDomainRepository
            )
        }
    
    @Test
    fun `nominal case - actual currency is euro should trigger change to dollar`() =
        testCoroutineRule.runTest {
            coEvery { currencyDomainRepository.getActualCurrencyEntity() } returns EntityProvidinator.provideCurrencyEntity()
                .copy(currencyEnum = CurrencyEnum.EURO)
            
            useCase.invoke()
            
            coVerify {
                currencyDomainRepository.getActualCurrencyEntity()
                currencyDomainRepository.insertCurrencyEntity(CurrencyEntity(CurrencyEnum.US_DOLLAR))
            }
            
            confirmVerified(
                currencyDomainRepository
            )
        }
}