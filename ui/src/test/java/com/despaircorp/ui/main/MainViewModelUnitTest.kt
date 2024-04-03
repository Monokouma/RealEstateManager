package com.despaircorp.ui.main

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.estate.GetEstateWithPictureEntityAsFlowUseCase
import com.despaircorp.domain.estate.model.EstateEntity
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import com.despaircorp.domain.geocoder.GetAddressFromLatLngUseCase
import com.despaircorp.domain.real_estate_agent.DisconnectAgentUseCase
import com.despaircorp.domain.real_estate_agent.GetLoggedRealEstateAgentEntityUseCase
import com.despaircorp.domain.real_estate_agent.InsertCreatedAgentUseCase
import com.despaircorp.shared.R
import com.despaircorp.stubs.EntityProvidinator
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_ADDRESS
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_CITY
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ID
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_NAME
import com.despaircorp.stubs.EntityProvidinator.provideEstateWithPictureEntities
import com.despaircorp.stubs.EntityProvidinator.provideLoggedRealEstateAgentEntity
import com.despaircorp.stubs.ViewModelinator.provideMainViewModel
import com.despaircorp.ui.main.activity.CurrencyEnum
import com.despaircorp.ui.utils.ProfilePictureRandomizator
import com.despaircorp.ui.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.DecimalFormat

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
    private val getEstateWithPictureEntityAsFlowUseCase: GetEstateWithPictureEntityAsFlowUseCase =
        mockk()
    
    private val application: Application = mockk()
    private val getAddressFromLatLngUseCase: GetAddressFromLatLngUseCase = mockk()
    
    
    @Before
    fun setup() {
        coEvery { getEstateWithPictureEntityAsFlowUseCase.invoke() } returns flowOf(
            provideEstateWithPictureEntities()
        )
        coEvery { getLoggedRealEstateAgentEntityUseCase.invoke() } returns provideLoggedRealEstateAgentEntity()
        coEvery { getAddressFromLatLngUseCase.invoke(EntityProvidinator.DEFAULT_ESTATE_LOCATION) } returns mapOf(
            "city" to DEFAULT_ESTATE_CITY,
            "address" to DEFAULT_ESTATE_ADDRESS
        )
        every { application.getString(R.string.us_dollar_symbol) } returns "$"
        every { application.getString(R.string.euro_symbol) } returns "€"
    }
    
    @Test
    fun `nominal case - estate list in dollar`() = testCoroutineRule.runTest {
        
        val viewModel = provideMainViewModel(
            getLoggedRealEstateAgentEntityUseCase = getLoggedRealEstateAgentEntityUseCase,
            disconnectAgentUseCase = disconnectAgentUseCase,
            profilePictureRandomizator = profilePictureRandomizator,
            insertCreatedAgentUseCase = insertCreatedAgentUseCase,
            getEstateWithPictureEntityAsFlowUseCase,
            application,
            getAddressFromLatLngUseCase
        )
        
        viewModel.uiState.test {
            awaitItem()
            val result = awaitItem()
            
            assertThat(result).isEqualTo(
                MainState.MainStateView(
                    currentLoggedInAgent = provideLoggedRealEstateAgentEntity(),
                    error = Error(0, false),
                    onCreateAgentSuccess = OnCreateAgentSuccess(false, 0),
                    List(3) { estateWithPictureId ->
                        EstateWithPictureEntity(
                            EstateEntity(
                                id = estateWithPictureId,
                                description = EntityProvidinator.DEFAULT_ESTATE_DESC,
                                surface = EntityProvidinator.DEFAULT_ESTATE_SURFACE,
                                roomNumber = EntityProvidinator.DEFAULT_ESTATE_ROOM_NUMBER,
                                bathroomNumber = EntityProvidinator.DEFAULT_ESTATE_BATH_ROOM_NUMBER,
                                numberOfBedrooms = EntityProvidinator.DEFAULT_ESTATE_BED_ROOM_NUMBER,
                                location = EntityProvidinator.DEFAULT_ESTATE_LOCATION,
                                estateType = EntityProvidinator.DEFAULT_ESTATE_TYPE,
                                price = "$" + EntityProvidinator.DEFAULT_ESTATE_PRICE,
                                pointOfInterest = emptyList(),
                                sellingDate = null,
                                entryDate = EntityProvidinator.DEFAULT_ESTATE_ENTRY_DATE,
                                status = EntityProvidinator.DEFAULT_ESTATE_STATUS,
                                address = DEFAULT_ESTATE_ADDRESS,
                                city = DEFAULT_ESTATE_CITY
                            ),
                            emptyList()
                        )
                    }
                )
            )
            
            viewModel.onChangeCurrencyClicked()
            val resultEuro = awaitItem()
            
            assertThat(resultEuro).isEqualTo(
                MainState.MainStateView(
                    currentLoggedInAgent = provideLoggedRealEstateAgentEntity(),
                    error = Error(0, false),
                    onCreateAgentSuccess = OnCreateAgentSuccess(false, 0),
                    List(3) { estateWithPictureId ->
                        EstateWithPictureEntity(
                            EstateEntity(
                                id = estateWithPictureId,
                                description = EntityProvidinator.DEFAULT_ESTATE_DESC,
                                surface = EntityProvidinator.DEFAULT_ESTATE_SURFACE,
                                roomNumber = EntityProvidinator.DEFAULT_ESTATE_ROOM_NUMBER,
                                bathroomNumber = EntityProvidinator.DEFAULT_ESTATE_BATH_ROOM_NUMBER,
                                numberOfBedrooms = EntityProvidinator.DEFAULT_ESTATE_BED_ROOM_NUMBER,
                                location = EntityProvidinator.DEFAULT_ESTATE_LOCATION,
                                estateType = EntityProvidinator.DEFAULT_ESTATE_TYPE,
                                price = getEuroFromDollar(EntityProvidinator.DEFAULT_ESTATE_PRICE) + "€",
                                pointOfInterest = emptyList(),
                                sellingDate = null,
                                entryDate = EntityProvidinator.DEFAULT_ESTATE_ENTRY_DATE,
                                status = EntityProvidinator.DEFAULT_ESTATE_STATUS,
                                address = DEFAULT_ESTATE_ADDRESS,
                                city = DEFAULT_ESTATE_CITY
                            ),
                            emptyList()
                        )
                    }
                )
            )
        }
    }
    
    @Test
    fun `nominal case - change currency to euro`() = testCoroutineRule.runTest {
        
        val viewModel = provideMainViewModel(
            getLoggedRealEstateAgentEntityUseCase = getLoggedRealEstateAgentEntityUseCase,
            disconnectAgentUseCase = disconnectAgentUseCase,
            profilePictureRandomizator = profilePictureRandomizator,
            insertCreatedAgentUseCase = insertCreatedAgentUseCase,
            getEstateWithPictureEntityAsFlowUseCase,
            application,
            getAddressFromLatLngUseCase
        )
        
        viewModel.onChangeCurrencyClicked()
        assertThat(viewModel.actualCurrencyMutableStateFlow.value).isEqualTo(CurrencyEnum.EURO)
        viewModel.onChangeCurrencyClicked()
        assertThat(viewModel.actualCurrencyMutableStateFlow.value).isEqualTo(CurrencyEnum.US_DOLLAR)
    }
    
    @Test
    fun `nominal case - init should return agent entity with no error`() =
        testCoroutineRule.runTest {
            coEvery { getLoggedRealEstateAgentEntityUseCase.invoke() } returns provideLoggedRealEstateAgentEntity()
            
            val viewModel = provideMainViewModel(
                getLoggedRealEstateAgentEntityUseCase = getLoggedRealEstateAgentEntityUseCase,
                disconnectAgentUseCase = disconnectAgentUseCase,
                profilePictureRandomizator = profilePictureRandomizator,
                insertCreatedAgentUseCase = insertCreatedAgentUseCase,
                getEstateWithPictureEntityAsFlowUseCase,
                application,
                getAddressFromLatLngUseCase
            )
            
            viewModel.uiState.test {
                awaitItem()
                val result = awaitItem()
                assertThat(result).isEqualTo(
                    MainState.MainStateView(
                        currentLoggedInAgent = provideLoggedRealEstateAgentEntity(),
                        error = Error(0, false),
                        onCreateAgentSuccess = OnCreateAgentSuccess(false, 0),
                        List(3) { estateWithPictureId ->
                            EstateWithPictureEntity(
                                EstateEntity(
                                    id = estateWithPictureId,
                                    description = EntityProvidinator.DEFAULT_ESTATE_DESC,
                                    surface = EntityProvidinator.DEFAULT_ESTATE_SURFACE,
                                    roomNumber = EntityProvidinator.DEFAULT_ESTATE_ROOM_NUMBER,
                                    bathroomNumber = EntityProvidinator.DEFAULT_ESTATE_BATH_ROOM_NUMBER,
                                    numberOfBedrooms = EntityProvidinator.DEFAULT_ESTATE_BED_ROOM_NUMBER,
                                    location = EntityProvidinator.DEFAULT_ESTATE_LOCATION,
                                    estateType = EntityProvidinator.DEFAULT_ESTATE_TYPE,
                                    price = "$" + EntityProvidinator.DEFAULT_ESTATE_PRICE,
                                    pointOfInterest = emptyList(),
                                    sellingDate = null,
                                    entryDate = EntityProvidinator.DEFAULT_ESTATE_ENTRY_DATE,
                                    status = EntityProvidinator.DEFAULT_ESTATE_STATUS,
                                    address = DEFAULT_ESTATE_ADDRESS,
                                    city = DEFAULT_ESTATE_CITY
                                ),
                                emptyList()
                            )
                        }
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
            insertCreatedAgentUseCase = insertCreatedAgentUseCase,
            getEstateWithPictureEntityAsFlowUseCase,
            application,
            getAddressFromLatLngUseCase
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
            insertCreatedAgentUseCase = insertCreatedAgentUseCase,
            getEstateWithPictureEntityAsFlowUseCase,
            application,
            getAddressFromLatLngUseCase
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
                    onCreateAgentSuccess = OnCreateAgentSuccess(false, 0),
                    emptyList()
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
            insertCreatedAgentUseCase = insertCreatedAgentUseCase,
            getEstateWithPictureEntityAsFlowUseCase,
            application,
            getAddressFromLatLngUseCase
        )
        
        viewModel.uiState.test {
            awaitItem() //Loading state
            awaitItem()
            viewModel.onRealEstateAgentNameTextChange(DEFAULT_NAME)
            viewModel.onCreateAgentClick()
            val result = awaitItem()
            
            assertThat(result).isEqualTo(
                MainState.MainStateView(
                    currentLoggedInAgent = provideLoggedRealEstateAgentEntity(),
                    error = Error(0, false),
                    onCreateAgentSuccess = OnCreateAgentSuccess(true, R.string.agent_created),
                    emptyList()
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
            insertCreatedAgentUseCase = insertCreatedAgentUseCase,
            getEstateWithPictureEntityAsFlowUseCase,
            application,
            getAddressFromLatLngUseCase
        )
        
        viewModel.uiState.test {
            awaitItem() //Loading state
            awaitItem()
            viewModel.onRealEstateAgentNameTextChange(DEFAULT_NAME)
            viewModel.onCreateAgentClick()
            
            val result = awaitItem()
            
            assertThat(result).isEqualTo(
                MainState.MainStateView(
                    currentLoggedInAgent = provideLoggedRealEstateAgentEntity(),
                    error = Error(R.string.error, true),
                    onCreateAgentSuccess = OnCreateAgentSuccess(false, 0),
                    emptyList()
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
            insertCreatedAgentUseCase = insertCreatedAgentUseCase,
            getEstateWithPictureEntityAsFlowUseCase,
            application,
            getAddressFromLatLngUseCase
        )
        
        viewModel.uiState.test {
            awaitItem() //Loading state
            awaitItem()
            viewModel.onRealEstateAgentNameTextChange("")
            viewModel.onCreateAgentClick()
            val result = awaitItem()
            
            assertThat(result).isEqualTo(
                MainState.MainStateView(
                    currentLoggedInAgent = provideLoggedRealEstateAgentEntity(),
                    error = Error(R.string.empty_text, true),
                    onCreateAgentSuccess = OnCreateAgentSuccess(false, 0),
                    emptyList()
                )
            )
        }
    }
    
    fun getEuroFromDollar(dollars: String): String {
        val dollarsInt = dollars.replace(".", "").toInt()
        val rate = 0.9307
        
        return DecimalFormat("#,###,###")
            .format((dollarsInt * rate))
            .replace(",", ".")
    }
}