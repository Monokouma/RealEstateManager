package com.despaircorp.data.estate.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.despaircorp.domain.estate.EstateDomainRepository
import com.despaircorp.domain.estate.model.EstateEntity
import com.despaircorp.domain.estate.model.EstateStatus
import com.despaircorp.domain.estate.model.EstateTypeEnum
import com.despaircorp.domain.estate.model.PointOfInterestEntity
import com.despaircorp.domain.estate.model.PointOfInterestEnum
import com.despaircorp.shared.R
import com.google.android.gms.maps.model.LatLng
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate

@HiltWorker
class EstateInitWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val estateDomainRepository: EstateDomainRepository,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        if (!estateDomainRepository.isTableExisting()) {
            estateDomainRepository.prePopulateEstateTable(
                listOf(
                    EstateEntity(
                        id = 1,
                        description = applicationContext.getString(R.string.first_house_desc),
                        surface = 110,
                        roomNumber = 7,
                        bathroomNumber = 2,
                        numberOfBedrooms = 3,
                        
                        location = LatLng(35.678186, 139.771044),
                        estateType = EstateTypeEnum.HOUSE,
                        price = "600000",
                        pointOfInterest = listOf(
                            PointOfInterestEntity(
                                isSelected = true,
                                pointOfInterestEnum = PointOfInterestEnum.SHOP,
                                id = PointOfInterestEnum.SHOP.id
                            ),
                            PointOfInterestEntity(
                                isSelected = true,
                                pointOfInterestEnum = PointOfInterestEnum.SCHOOLS,
                                id = PointOfInterestEnum.SCHOOLS.id
                            )
                        ),
                        sellingDate = null,
                        entryDate = LocalDate.of(2024, 1, 12),
                        status = EstateStatus.FOR_SALE,
                        address = "Tokyo Kyobashi, 1-chōme-3 Kyōbashi, Chuo City, Tokyo 104-0031, Japan",
                        city = "Chuo City",
                        1
                    ),
                    EstateEntity(
                        id = 2,
                        description = applicationContext.getString(R.string.second_house_desc),
                        surface = 220,
                        roomNumber = 10,
                        bathroomNumber = 3,
                        numberOfBedrooms = 4,
                        location = LatLng(34.050151, -118.317665),
                        estateType = EstateTypeEnum.HOUSE,
                        price = "500000",
                        pointOfInterest = listOf(
                            PointOfInterestEntity(
                                isSelected = true,
                                pointOfInterestEnum = PointOfInterestEnum.PARK,
                                id = PointOfInterestEnum.PARK.id
                            ),
                            
                            ),
                        sellingDate = null,
                        entryDate = LocalDate.of(1980, 6, 21),
                        status = EstateStatus.FOR_SALE,
                        address = "1109 Westchester Pl, Los Angeles, CA 90019, USA",
                        city = "Los Angeles",
                        2
                    ),
                    EstateEntity(
                        id = 3,
                        description = applicationContext.getString(R.string.third_house_desc),
                        surface = 90,
                        roomNumber = 6,
                        bathroomNumber = 1,
                        numberOfBedrooms = 2,
                        location = LatLng(48.859520, 2.271824),
                        estateType = EstateTypeEnum.APARTMENT,
                        price = "1000000",
                        pointOfInterest = listOf(
                            PointOfInterestEntity(
                                isSelected = true,
                                pointOfInterestEnum = PointOfInterestEnum.PARK,
                                id = PointOfInterestEnum.PARK.id
                            ),
                            PointOfInterestEntity(
                                isSelected = true,
                                pointOfInterestEnum = PointOfInterestEnum.SCHOOLS,
                                id = PointOfInterestEnum.SCHOOLS.id
                            ),
                            PointOfInterestEntity(
                                isSelected = true,
                                pointOfInterestEnum = PointOfInterestEnum.SUBWAY,
                                id = PointOfInterestEnum.SUBWAY.id
                            )
                        ),
                        sellingDate = null,
                        entryDate = LocalDate.of(2017, 11, 25),
                        status = EstateStatus.FOR_SALE,
                        address = "18 Rue Maspéro, 75116 Paris, France",
                        city = "Paris",
                        3
                    ),
                    EstateEntity(
                        id = 4,
                        description = applicationContext.getString(R.string.fourth_house_desc),
                        surface = 130,
                        roomNumber = 11,
                        bathroomNumber = 3,
                        numberOfBedrooms = 4,
                        location = LatLng(40.720122, -73.989974),
                        estateType = EstateTypeEnum.LOFT,
                        price = "750000",
                        pointOfInterest = listOf(
                            PointOfInterestEntity(
                                isSelected = true,
                                pointOfInterestEnum = PointOfInterestEnum.PARK,
                                id = PointOfInterestEnum.PARK.id
                            ),
                            PointOfInterestEntity(
                                isSelected = true,
                                pointOfInterestEnum = PointOfInterestEnum.SCHOOLS,
                                id = PointOfInterestEnum.SCHOOLS.id
                            ),
                            PointOfInterestEntity(
                                isSelected = true,
                                pointOfInterestEnum = PointOfInterestEnum.SUBWAY,
                                id = PointOfInterestEnum.SUBWAY.id
                            )
                        ),
                        sellingDate = null,
                        entryDate = LocalDate.of(2022, 10, 11),
                        status = EstateStatus.FOR_SALE,
                        address = "133 Allen St, New York, NY 10002, USA",
                        city = "New York",
                        1
                    ),
                    EstateEntity(
                        id = 5,
                        description = applicationContext.getString(R.string.fifth_house_desc),
                        surface = 230,
                        roomNumber = 8,
                        bathroomNumber = 2,
                        numberOfBedrooms = 4,
                        location = LatLng(45.080624, 4.775859),
                        estateType = EstateTypeEnum.MANOR,
                        price = "1750000",
                        pointOfInterest = listOf(
                            PointOfInterestEntity(
                                isSelected = true,
                                pointOfInterestEnum = PointOfInterestEnum.PARK,
                                id = PointOfInterestEnum.PARK.id
                            ),
                            PointOfInterestEntity(
                                isSelected = true,
                                pointOfInterestEnum = PointOfInterestEnum.SCHOOLS,
                                id = PointOfInterestEnum.SCHOOLS.id
                            )
                        ),
                        sellingDate = null,
                        entryDate = LocalDate.of(2022, 1, 11),
                        status = EstateStatus.FOR_SALE,
                        address = "6 Chem. de Monéron, 07300 Saint-Jean-de-Muzols, France",
                        city = "Saint-Jean-De-Muzols",
                        1
                    ),
                )
            )
        }
        return Result.success()
    }
}