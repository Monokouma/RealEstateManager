package com.despaircorp.domain.estate

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.estate.model.EstateCreationStatus
import com.despaircorp.domain.estate.model.PointOfInterestEntity
import com.despaircorp.domain.estate.model.PointOfInterestEnum
import com.despaircorp.domain.utils.TestCoroutineRule
import com.despaircorp.stubs.EntityProvidinator
import org.junit.Rule
import org.junit.Test

class SanitizeCreatedEstateUseCaseUnitTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    
    
    private val useCase = SanitizeCreatedEstateUseCase()
    
    @Test
    fun `nominal case - success`() = testCoroutineRule.runTest {
        val result = useCase.invoke(EntityProvidinator.provideToSanitizeEntity())
        
        assertThat(result).isEqualTo(EstateCreationStatus.SUCCESS)
        
    }
    
    @Test
    fun `edge case - empty surface`() = testCoroutineRule.runTest {
        val result =
            useCase.invoke(EntityProvidinator.provideToSanitizeEntity().copy(estateSurface = ""))
        
        assertThat(result).isEqualTo(EstateCreationStatus.EMPTY_SURFACE)
        
    }
    
    @Test
    fun `edge case - empty description`() = testCoroutineRule.runTest {
        val result =
            useCase.invoke(
                EntityProvidinator.provideToSanitizeEntity().copy(estateDescription = "")
            )
        
        assertThat(result).isEqualTo(EstateCreationStatus.EMPTY_DESCRIPTION)
    }
    
    @Test
    fun `edge case - empty room number`() = testCoroutineRule.runTest {
        val result =
            useCase.invoke(EntityProvidinator.provideToSanitizeEntity().copy(estateRoomNumber = ""))
        
        assertThat(result).isEqualTo(EstateCreationStatus.EMPTY_ROOM_NUMBER)
    }
    
    @Test
    fun `edge case - empty bed room number`() = testCoroutineRule.runTest {
        val result =
            useCase.invoke(
                EntityProvidinator.provideToSanitizeEntity().copy(estateBedroomNumber = "")
            )
        
        assertThat(result).isEqualTo(EstateCreationStatus.EMPTY_BEDROOM_NUMBER)
    }
    
    @Test
    fun `edge case - empty bath room number`() = testCoroutineRule.runTest {
        val result =
            useCase.invoke(
                EntityProvidinator.provideToSanitizeEntity().copy(estateBathRoomNumber = "")
            )
        
        assertThat(result).isEqualTo(EstateCreationStatus.EMPTY_BATHROOM_NUMBER)
    }
    
    @Test
    fun `edge case - empty address`() = testCoroutineRule.runTest {
        val result =
            useCase.invoke(
                EntityProvidinator.provideToSanitizeEntity().copy(estateAddress = "")
            )
        
        assertThat(result).isEqualTo(EstateCreationStatus.EMPTY_ADDRESS)
    }
    
    
    @Test
    fun `edge case - empty city`() = testCoroutineRule.runTest {
        val result =
            useCase.invoke(
                EntityProvidinator.provideToSanitizeEntity().copy(estateCity = "")
            )
        
        assertThat(result).isEqualTo(EstateCreationStatus.EMPTY_CITY)
    }
    
    
    @Test
    fun `edge case - empty price`() = testCoroutineRule.runTest {
        val result =
            useCase.invoke(
                EntityProvidinator.provideToSanitizeEntity().copy(estatePrice = "")
            )
        
        assertThat(result).isEqualTo(EstateCreationStatus.EMPTY_PRICE)
    }
    
    
    @Test
    fun `edge case - empty point of interest`() = testCoroutineRule.runTest {
        val result =
            useCase.invoke(
                EntityProvidinator.provideToSanitizeEntity().copy(
                    estatePointOfInterests = listOf(
                        PointOfInterestEntity(
                            isSelected = false,
                            pointOfInterestEnum = PointOfInterestEnum.SUBWAY,
                            id = 1
                        )
                    )
                )
            )
        
        assertThat(result).isEqualTo(EstateCreationStatus.EMPTY_POINT_OF_INTEREST)
    }
    
    @Test
    fun `edge case - entry date`() = testCoroutineRule.runTest {
        val result =
            useCase.invoke(
                EntityProvidinator.provideToSanitizeEntity().copy(estateEntryDate = "")
            )
        
        assertThat(result).isEqualTo(EstateCreationStatus.EMPTY_ENTRY_DATE)
        
        val resultSecond =
            useCase.invoke(
                EntityProvidinator.provideToSanitizeEntity().copy(estateEntryDate = "a text")
            )
        
        assertThat(resultSecond).isEqualTo(EstateCreationStatus.EMPTY_ENTRY_DATE)
    }
    
    @Test
    fun `edge case - sold date`() = testCoroutineRule.runTest {
        val result =
            useCase.invoke(
                EntityProvidinator.provideToSanitizeEntity()
                    .copy(estateSoldDate = "", isEstateSold = true)
            )
        
        assertThat(result).isEqualTo(EstateCreationStatus.EMPTY_SOLD_DATE)
        
        val resultSecond =
            useCase.invoke(
                EntityProvidinator.provideToSanitizeEntity()
                    .copy(estateSoldDate = "a text", isEstateSold = true)
            )
        
        assertThat(resultSecond).isEqualTo(EstateCreationStatus.EMPTY_SOLD_DATE)
    }
    
    @Test
    fun `edge case - empty picture`() = testCoroutineRule.runTest {
        val result =
            useCase.invoke(
                EntityProvidinator.provideToSanitizeEntity().copy(
                    estatePictures = emptyList()
                )
            )
        
        assertThat(result).isEqualTo(EstateCreationStatus.EMPTY_PICTURE)
    }
}
