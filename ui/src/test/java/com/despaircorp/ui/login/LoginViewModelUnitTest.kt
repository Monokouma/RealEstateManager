package com.despaircorp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.real_estate_agent.GetRealEstateAgentEntitiesUseCase
import com.despaircorp.domain.real_estate_agent.IsAgentCurrentlyLoggedInUseCase
import com.despaircorp.domain.real_estate_agent.LogChosenAgentUseCase
import com.despaircorp.domain.splash_screen.CountDownSplashScreenUseCase
import com.despaircorp.shared.R
import com.despaircorp.stubs.EntityProvidinator.provideRealEstateAgentEntities
import com.despaircorp.stubs.ViewModelinator.provideLoginViewModel
import com.despaircorp.ui.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test


class LoginViewModelUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private val countDownSplashScreenUseCase: CountDownSplashScreenUseCase = mockk()
    private val getRealEstateAgentEntitiesUseCase: GetRealEstateAgentEntitiesUseCase = mockk()
    private val logChosenAgentUseCase: LogChosenAgentUseCase = mockk()
    private val isAgentCurrentlyLoggedInUseCase: IsAgentCurrentlyLoggedInUseCase = mockk()
    
    companion object {
        private const val DEFAULT_AGENT_ID = 1
    }
    
    @Test
    fun `nominal case - log agent success`() = testCoroutineRule.runTest {
        coEvery { getRealEstateAgentEntitiesUseCase.invoke() } returns flowOf(
            provideRealEstateAgentEntities()
        )
        coEvery { isAgentCurrentlyLoggedInUseCase.invoke() } returns false
        coEvery { countDownSplashScreenUseCase.invoke() } returns true
        
        coEvery { logChosenAgentUseCase.invoke(DEFAULT_AGENT_ID) } returns true
        
        val viewModel = provideLoginViewModel(
            countDownSplashScreenUseCase,
            getRealEstateAgentEntitiesUseCase,
            logChosenAgentUseCase,
            isAgentCurrentlyLoggedInUseCase
        )
        
        
        
        viewModel.uiStateFlow.test {
            awaitItem()
            viewModel.onSelectedRealEstateAgent(DEFAULT_AGENT_ID)
            awaitItem()
            val result = awaitItem()
            
            assertThat(result).isEqualTo(
                LoginState.SuccessLogin
            )
        }
    }
    
    @Test
    fun `error case - log agent failure`() = testCoroutineRule.runTest {
        coEvery { getRealEstateAgentEntitiesUseCase.invoke() } returns flowOf(
            provideRealEstateAgentEntities()
        )
        coEvery { isAgentCurrentlyLoggedInUseCase.invoke() } returns false
        coEvery { countDownSplashScreenUseCase.invoke() } returns true
        
        coEvery { logChosenAgentUseCase.invoke(DEFAULT_AGENT_ID) } returns false
        
        val viewModel = provideLoginViewModel(
            countDownSplashScreenUseCase,
            getRealEstateAgentEntitiesUseCase,
            logChosenAgentUseCase,
            isAgentCurrentlyLoggedInUseCase
        )
        
        viewModel.onSelectedRealEstateAgent(DEFAULT_AGENT_ID)
        
        viewModel.uiStateFlow.test {
            awaitItem()
            viewModel.onSelectedRealEstateAgent(DEFAULT_AGENT_ID)
            awaitItem()
            val result = awaitItem()
            
            assertThat(result).isEqualTo(
                LoginState.Error(R.string.error)
            )
        }
    }
    
    @Test
    fun `nominal case - no agent logged should show list`() = testCoroutineRule.runTest {
        coEvery { getRealEstateAgentEntitiesUseCase.invoke() } returns flowOf(
            provideRealEstateAgentEntities()
        )
        coEvery { isAgentCurrentlyLoggedInUseCase.invoke() } returns false
        coEvery { countDownSplashScreenUseCase.invoke() } returns true
        
        val viewModel = provideLoginViewModel(
            countDownSplashScreenUseCase,
            getRealEstateAgentEntitiesUseCase,
            logChosenAgentUseCase,
            isAgentCurrentlyLoggedInUseCase
        )
        
        viewModel.uiStateFlow.test {
            awaitItem()
            
            val result = awaitItem()
            
            assertThat(result).isEqualTo(
                LoginState.ShowRealEstateAgentEntities(
                    provideRealEstateAgentEntities()
                )
            )
        }
    }
    
    @Test
    fun `nominal case - count down return false should show splash screen`() =
        testCoroutineRule.runTest {
            coEvery { getRealEstateAgentEntitiesUseCase.invoke() } returns flowOf(
                provideRealEstateAgentEntities()
            )
            coEvery { isAgentCurrentlyLoggedInUseCase.invoke() } returns false
            coEvery { countDownSplashScreenUseCase.invoke() } returns false
            
            val viewModel = provideLoginViewModel(
                countDownSplashScreenUseCase,
                getRealEstateAgentEntitiesUseCase,
                logChosenAgentUseCase,
                isAgentCurrentlyLoggedInUseCase
            )
            
            viewModel.uiStateFlow.test {
                val result = awaitItem()
                
                assertThat(result).isEqualTo(
                    LoginState.CountDown
                )
            }
        }
    
    @Test
    fun `nominal case - logged agent should go next screen`() = testCoroutineRule.runTest {
        coEvery { getRealEstateAgentEntitiesUseCase.invoke() } returns flowOf(
            provideRealEstateAgentEntities()
        )
        coEvery { isAgentCurrentlyLoggedInUseCase.invoke() } returns true
        coEvery { countDownSplashScreenUseCase.invoke() } returns true
        
        val viewModel = provideLoginViewModel(
            countDownSplashScreenUseCase,
            getRealEstateAgentEntitiesUseCase,
            logChosenAgentUseCase,
            isAgentCurrentlyLoggedInUseCase
        )
        
        viewModel.uiStateFlow.test {
            awaitItem()
            val result = awaitItem()
            
            assertThat(result).isEqualTo(
                LoginState.AlreadyLoggedInAgent
            )
        }
    }
}