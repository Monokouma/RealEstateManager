package com.despaircorp.ui

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.connectivity.IsUserConnectedToInternetUseCase
import com.despaircorp.domain.currency.GetActualCurrencyUseCase
import com.despaircorp.domain.currency.model.CurrencyEntity
import com.despaircorp.domain.currency.model.CurrencyEnum
import com.despaircorp.domain.estate.GetEstateWithPictureEntityAsFlowUseCase
import com.despaircorp.domain.filter.EstateSqlQueryBuilderUseCase
import com.despaircorp.domain.filter.OnFilterChangedUseCase
import com.despaircorp.domain.geocoder.GetAddressFromLatLngUseCase
import com.despaircorp.domain.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_BITMAP_FILE_PATH
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_ADDRESS
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_CITY
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_LOCATION
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_TYPE
import com.despaircorp.stubs.EntityProvidinator.provideCurrencyEntity
import com.despaircorp.stubs.EntityProvidinator.provideEstateWithPictureEntities
import com.despaircorp.ui.main.master_fragment.MasterViewModel
import com.despaircorp.ui.main.master_fragment.MasterViewState
import com.despaircorp.ui.main.master_fragment.estate.EstateViewStateItems
import com.despaircorp.ui.utils.observeForTesting
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MasterViewModelUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private val getEstateWithPictureEntityAsFlowUseCase: GetEstateWithPictureEntityAsFlowUseCase =
        mockk()
    private val getAddressFromLatLngUseCase: GetAddressFromLatLngUseCase = mockk()
    private val application: Application = mockk()
    private val getActualCurrencyUseCase: GetActualCurrencyUseCase = mockk()
    private val isUserConnectedToInternetUseCase: IsUserConnectedToInternetUseCase = mockk()
    private val onFilterChangedUseCase: OnFilterChangedUseCase = mockk()
    private val estateSqlQueryBuilderUseCase: EstateSqlQueryBuilderUseCase = mockk()
    
    private lateinit var viewModel: MasterViewModel
    
    @Before
    fun setup() {
        
        coEvery { getActualCurrencyUseCase.invoke() } returns flowOf(provideCurrencyEntity())
        coEvery { isUserConnectedToInternetUseCase.invoke() } returns flowOf(true)
        coEvery { getEstateWithPictureEntityAsFlowUseCase.invoke(any()) } returns flowOf(
            provideEstateWithPictureEntities()
        )
        coEvery { getAddressFromLatLngUseCase.invoke(DEFAULT_ESTATE_LOCATION) } returns mapOf(
            "city" to DEFAULT_ESTATE_CITY,
            "address" to DEFAULT_ESTATE_ADDRESS
        )
        every { application.getString(DEFAULT_ESTATE_TYPE.textRes) } returns "type"
        every { application.getString(provideCurrencyEntity().currencyEnum.symbolResource) } returns "$"
        every { application.resources.getBoolean(R.bool.isLandscape) } returns true
        viewModel = MasterViewModel(
            getEstateWithPictureEntityAsFlowUseCase = getEstateWithPictureEntityAsFlowUseCase,
            getAddressFromLatLngUseCase = getAddressFromLatLngUseCase,
            application = application,
            getActualCurrencyUseCase = getActualCurrencyUseCase,
            isUserConnectedToInternetUseCase = isUserConnectedToInternetUseCase,
            onFilterChangedUseCase = onFilterChangedUseCase,
            estateSqlQueryBuilderUseCase = estateSqlQueryBuilderUseCase
        )
        
    }
    
    @Test
    fun `nominal case - get entities list with currency dollar`() = testCoroutineRule.runTest {
        
        viewModel.viewState.observeForTesting(this) {
            
            assertThat(it.value).isEqualTo(
                MasterViewState(
                    estateViewStateItems = listOf(
                        EstateViewStateItems(
                            id = 0,
                            picture = DEFAULT_BITMAP_FILE_PATH,
                            city = DEFAULT_ESTATE_CITY,
                            type = "type",
                            price = "$10,000",
                            isSelected = false
                        ),
                        EstateViewStateItems(
                            id = 1,
                            picture = DEFAULT_BITMAP_FILE_PATH,
                            city = DEFAULT_ESTATE_CITY,
                            type = "type",
                            price = "$10,000",
                            isSelected = true
                        ),
                        EstateViewStateItems(
                            id = 2,
                            picture = DEFAULT_BITMAP_FILE_PATH,
                            city = DEFAULT_ESTATE_CITY,
                            type = "type",
                            price = "$10,000",
                            isSelected = false
                        )
                    )
                )
            )
        }
        
    }
    
    @Test
    fun `nominal case - get entities list with currency euro`() = testCoroutineRule.runTest {
        coEvery { getActualCurrencyUseCase.invoke() } returns flowOf(
            CurrencyEntity(
                CurrencyEnum.EURO
            )
        )
        
        every { application.getString(provideCurrencyEntity().copy(currencyEnum = CurrencyEnum.EURO).currencyEnum.symbolResource) } returns "€"
        
        viewModel.viewState.observeForTesting(this) {
            assertThat(it.value).isEqualTo(
                MasterViewState(
                    estateViewStateItems = listOf(
                        EstateViewStateItems(
                            id = 0,
                            picture = DEFAULT_BITMAP_FILE_PATH,
                            city = DEFAULT_ESTATE_CITY,
                            type = "type",
                            price = "9 307€",
                            isSelected = false
                        ),
                        EstateViewStateItems(
                            id = 1,
                            picture = DEFAULT_BITMAP_FILE_PATH,
                            city = DEFAULT_ESTATE_CITY,
                            type = "type",
                            price = "9 307€",
                            isSelected = true
                        ),
                        EstateViewStateItems(
                            id = 2,
                            picture = DEFAULT_BITMAP_FILE_PATH,
                            city = DEFAULT_ESTATE_CITY,
                            type = "type",
                            price = "9 307€",
                            isSelected = false
                        )
                    )
                )
            )
            
        }
        
    }
}