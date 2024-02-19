package com.despaircorp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.real_estate_agent.GetRealEstateAgentEntitiesUseCase
import com.despaircorp.domain.real_estate_agent.IsAgentCurrentlyLoggedInUseCase
import com.despaircorp.domain.real_estate_agent.LogChosenAgentUseCase
import com.despaircorp.domain.splash_screen.CountDownSplashScreenUseCase
import com.despaircorp.stubs.EntityProvidinator.provideRealEstateAgentEntities
import com.despaircorp.ui.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@Ignore
class LoginViewModelUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private val countDownSplashScreenUseCase: CountDownSplashScreenUseCase = mockk()
    private val getRealEstateAgentEntitiesUseCase: GetRealEstateAgentEntitiesUseCase = mockk()
    private val logChosenAgentUseCase: LogChosenAgentUseCase = mockk()
    private val isAgentCurrentlyLoggedInUseCase: IsAgentCurrentlyLoggedInUseCase = mockk()
    
    private lateinit var viewModel: LoginViewModel
    
    companion object {
        private const val DEFAULT_AGENT_ID = 1
    }
    
    @Before
    fun setup() {
        coEvery { getRealEstateAgentEntitiesUseCase.invoke() } returns flowOf(
            provideRealEstateAgentEntities()
        )
        
        coEvery { logChosenAgentUseCase.invoke(DEFAULT_AGENT_ID) } returns true
        coEvery { countDownSplashScreenUseCase.invoke() } returns true
        
        
        coEvery { isAgentCurrentlyLoggedInUseCase.invoke() } returns true
        
        viewModel = LoginViewModel(
            countDownSplashScreenUseCase = countDownSplashScreenUseCase,
            getRealEstateAgentEntitiesUseCase = getRealEstateAgentEntitiesUseCase,
            logChosenAgentUseCase = logChosenAgentUseCase,
            isAgentCurrentlyLoggedInUseCase = isAgentCurrentlyLoggedInUseCase
        )
    }
    
    @Test
    fun `nominal case - no agent logged should show list`() = testCoroutineRule.runTest {
        viewModel.uiStateFlow.test {
            assertThat(awaitItem()).isEqualTo(
                LoginState.ShowRealEstateAgentEntities(
                    provideRealEstateAgentEntities()
                )
            )
        }
    }
    
    @Test
    fun `nominal case - already logged in agent`() = testCoroutineRule.runTest {
        coEvery { isAgentCurrentlyLoggedInUseCase.invoke() } returns false
        viewModel.uiStateFlow.test {
            
            println(awaitItem())
        }
    }
    
}