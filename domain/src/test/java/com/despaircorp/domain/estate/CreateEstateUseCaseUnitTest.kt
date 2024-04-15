package com.despaircorp.domain.estate

import android.app.Application
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.connectivity.IsUserConnectedToInternetUseCase
import com.despaircorp.domain.estate.model.EstateCreationStatus
import com.despaircorp.domain.estate.model.EstateEntity
import com.despaircorp.domain.estate.model.PointOfInterestEnum
import com.despaircorp.domain.geocoder.GetLatLngFromAddressUseCase
import com.despaircorp.domain.picture.InsertPictureForEstateCreationUseCase
import com.despaircorp.domain.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_ADDRESS
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_BATH_ROOM_NUMBER
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_BED_ROOM_NUMBER
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_CITY
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_DESC
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_ENTRY_DATE
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_LOCATION
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_PRICE
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_ROOM_NUMBER
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_SOLD_DATE
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_STATUS
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_SURFACE
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ESTATE_TYPE
import com.despaircorp.stubs.EntityProvidinator.DEFAULT_ID
import com.despaircorp.stubs.EntityProvidinator.provideToSanitizeEntity
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.format.DateTimeFormatter

class CreateEstateUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    private val estateDomainRepository: EstateDomainRepository = mockk()
    private val isUserConnectedToInternetUseCase: IsUserConnectedToInternetUseCase = mockk()
    private val sanitizeCreatedEstateUseCase: SanitizeCreatedEstateUseCase = mockk()
    private val application: Application = mockk()
    private val getLatLngFromAddressUseCase: GetLatLngFromAddressUseCase = mockk()
    private val insertPictureForEstateCreationUseCase: InsertPictureForEstateCreationUseCase =
        mockk()
    
    private val useCase = CreateEstateUseCase(
        estateDomainRepository = estateDomainRepository,
        isUserConnectedToInternetUseCase = isUserConnectedToInternetUseCase,
        sanitizeCreatedEstateUseCase = sanitizeCreatedEstateUseCase,
        application = application,
        getLatLngFromAddressUseCase = getLatLngFromAddressUseCase,
        insertPictureForEstateCreationUseCase = insertPictureForEstateCreationUseCase
    )
    
    @Before
    fun setup() {
        coEvery { estateDomainRepository.getEstateEntities() } returns EntityProvidinator.provideEstateEntities()
        coJustRun {
            estateDomainRepository.insertNewEstate(
                EstateEntity(
                    id = 3,
                    description = DEFAULT_ESTATE_DESC,
                    surface = "${DEFAULT_ESTATE_SURFACE}m2",
                    roomNumber = DEFAULT_ESTATE_ROOM_NUMBER,
                    bathroomNumber = DEFAULT_ESTATE_BATH_ROOM_NUMBER,
                    numberOfBedrooms = DEFAULT_ESTATE_BED_ROOM_NUMBER,
                    location = DEFAULT_ESTATE_LOCATION,
                    estateType = DEFAULT_ESTATE_TYPE,
                    price = DEFAULT_ESTATE_PRICE,
                    pointOfInterest = emptyList(),
                    sellingDate = null,
                    entryDate = DEFAULT_ESTATE_ENTRY_DATE,
                    status = DEFAULT_ESTATE_STATUS,
                    address = DEFAULT_ESTATE_ADDRESS,
                    city = DEFAULT_ESTATE_CITY,
                    agentInChargeId = EntityProvidinator.DEFAULT_AGENT_IN_CHARGE_ID
                )
            )
        }
        coEvery { isUserConnectedToInternetUseCase.invoke() } returns flowOf(true)
        coEvery {
            sanitizeCreatedEstateUseCase.invoke(
                provideToSanitizeEntity().copy(
                    estatePointOfInterests = emptyList(),
                    estatePictures = emptyList()
                )
            )
        } returns EstateCreationStatus.SUCCESS
        
        every { application.getString(PointOfInterestEnum.PARK.textRes) } returns DEFAULT_PARK_TEXT
        every { application.getString(PointOfInterestEnum.SCHOOLS.textRes) } returns DEFAULT_SCHOOLS_TEXT
        every { application.getString(PointOfInterestEnum.SHOP.textRes) } returns DEFAULT_SHOP_TEXT
        every { application.getString(PointOfInterestEnum.SUBWAY.textRes) } returns DEFAULT_SUBWAY_TEXT
        coEvery { getLatLngFromAddressUseCase.invoke("$DEFAULT_ESTATE_ADDRESS $DEFAULT_ESTATE_CITY") } returns DEFAULT_ESTATE_LOCATION
        coJustRun {
            insertPictureForEstateCreationUseCase.invoke(
                emptyList(),
                3
            )
        }
    }
    
    @Test
    fun `nominal case - success`() = testCoroutineRule.runTest {
        
        val result = useCase.invoke(
            DEFAULT_ESTATE_SURFACE,
            DEFAULT_ESTATE_DESC,
            DEFAULT_ESTATE_ROOM_NUMBER.toString(),
            DEFAULT_ESTATE_BED_ROOM_NUMBER.toString(),
            DEFAULT_ESTATE_BATH_ROOM_NUMBER.toString(),
            DEFAULT_ESTATE_ADDRESS,
            DEFAULT_ESTATE_CITY,
            DEFAULT_ESTATE_PRICE,
            DEFAULT_ESTATE_TYPE,
            emptyList(),
            false,
            DEFAULT_ESTATE_ENTRY_DATE.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            DEFAULT_ESTATE_SOLD_DATE.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            emptyList(),
            DEFAULT_ID
        )
        
        assertThat(result.getOrNull()).isEqualTo(true)
        
        
    }
    
    companion object {
        private const val DEFAULT_PARK_TEXT = "DEFAULT_PARK_TEXT"
        private const val DEFAULT_SCHOOLS_TEXT = "DEFAULT_SCHOOLS_TEXT"
        private const val DEFAULT_SHOP_TEXT = "DEFAULT_SHOP_TEXT"
        private const val DEFAULT_SUBWAY_TEXT = "DEFAULT_SUBWAY_TEXT"
        
        
    }
}