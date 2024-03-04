package com.despaircorp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.real_estate_agent.DisconnectAgentUseCase
import com.despaircorp.domain.real_estate_agent.GetLoggedRealEstateAgentEntityUseCase
import com.despaircorp.domain.real_estate_agent.InsertCreatedAgentUseCase
import com.despaircorp.shared.R
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ID
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_NAME
import com.despaircorp.stubs.EntityProvidinator.provideLoggedRealEstateAgentEntity
import com.despaircorp.stubs.ViewModelinator.provideMainViewModel
import com.despaircorp.ui.utils.ProfilePictureRandomizator
import com.despaircorp.ui.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class MainViewModelUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private val getLoggedRealEstateAgentEntityUseCase: GetLoggedRealEstateAgentEntityUseCase =
        mockk()
    private val disconnectAgentUseCase: DisconnectAgentUseCase = mockk()
    private val profilePictureRandomizator: ProfilePictureRandomizator = mockk()
    private val insertCreatedAgentUseCase: InsertCreatedAgentUseCase = mockk()
    
    @Test
    fun `nominal case - init should return agent entity with no error`() =
        testCoroutineRule.runTest {
            coEvery { getLoggedRealEstateAgentEntityUseCase.invoke() } returns provideLoggedRealEstateAgentEntity()
            
            val viewModel = provideMainViewModel(
                getLoggedRealEstateAgentEntityUseCase = getLoggedRealEstateAgentEntityUseCase,
                disconnectAgentUseCase = disconnectAgentUseCase,
                profilePictureRandomizator = profilePictureRandomizator,
                insertCreatedAgentUseCase = insertCreatedAgentUseCase
            )
            
            viewModel.uiState.test {
                awaitItem()
                val result = awaitItem()
                assertThat(result).isEqualTo(
                    MainState.MainStateView(
                        currentLoggedInAgent = provideLoggedRealEstateAgentEntity(),
                        error = Error(0, false),
                        onCreateAgentSuccess = OnCreateAgentSuccess(false, 0)
                    )
                )
            }
        }
    
    @Test
    fun `nominal case - on disconnect success`() = testCoroutineRule.runTest {
        coEvery { getLoggedRealEstateAgentEntityUseCase.invoke() } returns provideLoggedRealEstateAgentEntity()
        coEvery { disconnectAgentUseCase.invoke(DEFAULT_ID) } returns true
        
        val viewModel = provideMainViewModel(
            getLoggedRealEstateAgentEntityUseCase = getLoggedRealEstateAgentEntityUseCase,
            disconnectAgentUseCase = disconnectAgentUseCase,
            profilePictureRandomizator = profilePictureRandomizator,
            insertCreatedAgentUseCase = insertCreatedAgentUseCase
        )
        
        viewModel.uiState.test {
            awaitItem()
            awaitItem()
            viewModel.onDisconnect(DEFAULT_ID)
            val result = awaitItem()
            
            assertThat(result).isEqualTo(MainState.Disconnected)
        }
    }
    
    @Test
    fun `nominal case - on disconnect failure`() = testCoroutineRule.runTest {
        coEvery { getLoggedRealEstateAgentEntityUseCase.invoke() } returns provideLoggedRealEstateAgentEntity()
        coEvery { disconnectAgentUseCase.invoke(DEFAULT_ID) } returns false
        
        val viewModel = provideMainViewModel(
            getLoggedRealEstateAgentEntityUseCase = getLoggedRealEstateAgentEntityUseCase,
            disconnectAgentUseCase = disconnectAgentUseCase,
            profilePictureRandomizator = profilePictureRandomizator,
            insertCreatedAgentUseCase = insertCreatedAgentUseCase
        )
        
        viewModel.uiState.test {
            awaitItem()
            awaitItem()
            viewModel.onDisconnect(DEFAULT_ID)
            val result = awaitItem()
            
            assertThat(result).isEqualTo(
                MainState.MainStateView(
                    currentLoggedInAgent = provideLoggedRealEstateAgentEntity(),
                    error = Error(R.string.error, true),
                    onCreateAgentSuccess = OnCreateAgentSuccess(false, 0)
                )
            )
        }
    }
    
    @Test
    fun `nominal case - on create agent success`() = testCoroutineRule.runTest {
        coEvery { getLoggedRealEstateAgentEntityUseCase.invoke() } returns provideLoggedRealEstateAgentEntity()
        coEvery { profilePictureRandomizator.provideRandomProfilePicture() } returns R.drawable.lb
        coEvery { insertCreatedAgentUseCase.invoke(DEFAULT_NAME, R.drawable.lb) } returns true
        
        val viewModel = provideMainViewModel(
            getLoggedRealEstateAgentEntityUseCase = getLoggedRealEstateAgentEntityUseCase,
            disconnectAgentUseCase = disconnectAgentUseCase,
            profilePictureRandomizator = profilePictureRandomizator,
            insertCreatedAgentUseCase = insertCreatedAgentUseCase
        )
        
        viewModel.uiState.test {
            awaitItem() //Loading state
            viewModel.onRealEstateAgentNameTextChange(DEFAULT_NAME)
            viewModel.onCreateAgentClick()
            awaitItem()
            val result = awaitItem()
            
            assertThat(result).isEqualTo(
                MainState.MainStateView(
                    currentLoggedInAgent = provideLoggedRealEstateAgentEntity(),
                    error = Error(0, false),
                    onCreateAgentSuccess = OnCreateAgentSuccess(true, R.string.agent_created)
                )
            )
        }
    }
    
    @Test
    fun `nominal case - on create agent failure`() = testCoroutineRule.runTest {
        coEvery { getLoggedRealEstateAgentEntityUseCase.invoke() } returns provideLoggedRealEstateAgentEntity()
        coEvery { profilePictureRandomizator.provideRandomProfilePicture() } returns R.drawable.lb
        coEvery { insertCreatedAgentUseCase.invoke(DEFAULT_NAME, R.drawable.lb) } returns false
        
        val viewModel = provideMainViewModel(
            getLoggedRealEstateAgentEntityUseCase = getLoggedRealEstateAgentEntityUseCase,
            disconnectAgentUseCase = disconnectAgentUseCase,
            profilePictureRandomizator = profilePictureRandomizator,
            insertCreatedAgentUseCase = insertCreatedAgentUseCase
        )
        
        viewModel.uiState.test {
            awaitItem() //Loading state
            viewModel.onRealEstateAgentNameTextChange(DEFAULT_NAME)
            viewModel.onCreateAgentClick()
            awaitItem()
            val result = awaitItem()
            
            assertThat(result).isEqualTo(
                MainState.MainStateView(
                    currentLoggedInAgent = provideLoggedRealEstateAgentEntity(),
                    error = Error(R.string.error, true),
                    onCreateAgentSuccess = OnCreateAgentSuccess(false, 0)
                )
            )
        }
    }
    
    @Test
    fun `nominal case - on create agent empty name`() = testCoroutineRule.runTest {
        coEvery { getLoggedRealEstateAgentEntityUseCase.invoke() } returns provideLoggedRealEstateAgentEntity()
        coEvery { profilePictureRandomizator.provideRandomProfilePicture() } returns R.drawable.lb
        coEvery { insertCreatedAgentUseCase.invoke(DEFAULT_NAME, R.drawable.lb) } returns false
        
        val viewModel = provideMainViewModel(
            getLoggedRealEstateAgentEntityUseCase = getLoggedRealEstateAgentEntityUseCase,
            disconnectAgentUseCase = disconnectAgentUseCase,
            profilePictureRandomizator = profilePictureRandomizator,
            insertCreatedAgentUseCase = insertCreatedAgentUseCase
        )
        
        viewModel.uiState.test {
            awaitItem() //Loading state
            viewModel.onRealEstateAgentNameTextChange("")
            viewModel.onCreateAgentClick()
            awaitItem()
            val result = awaitItem()
            
            assertThat(result).isEqualTo(
                MainState.MainStateView(
                    currentLoggedInAgent = provideLoggedRealEstateAgentEntity(),
                    error = Error(R.string.empty_text, true),
                    onCreateAgentSuccess = OnCreateAgentSuccess(false, 0)
                )
            )
        }
    }
}