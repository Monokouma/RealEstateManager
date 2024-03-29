package com.despaircorp.data.estate.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.despaircorp.domain.estate.EstateDomainRepository
import com.despaircorp.domain.estate.model.EstateEntity
import com.despaircorp.domain.estate.model.EstateStatus
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
                        surface = "110m²",
                        roomNumber = 7,
                        bathroomNumber = 2,
                        numberOfBedrooms = 3,
                        location = LatLng(4.942028, -119.491757),
                        estateType = applicationContext.getString(R.string.house),
                        price = "600.000",
                        pointOfInterest = listOf(
                            applicationContext.getString(R.string.schools),
                            applicationContext.getString(R.string.shops)
                        ),
                        sellingDate = null,
                        entryDate = LocalDate.of(2024, 1, 12),
                        status = EstateStatus.FOR_SALE,
                    ),
                    EstateEntity(
                        id = 2,
                        description = applicationContext.getString(R.string.second_house_desc),
                        surface = "220m²",
                        roomNumber = 10,
                        bathroomNumber = 3,
                        numberOfBedrooms = 4,
                        location = LatLng(34.050151, -118.317665),
                        estateType = applicationContext.getString(R.string.house),
                        price = "500.000",
                        pointOfInterest = listOf(applicationContext.getString(R.string.parks)),
                        sellingDate = null,
                        entryDate = LocalDate.of(1980, 6, 21),
                        status = EstateStatus.FOR_SALE,
                    ),
                    EstateEntity(
                        id = 3,
                        description = applicationContext.getString(R.string.third_house_desc),
                        surface = "90m²",
                        roomNumber = 6,
                        bathroomNumber = 1,
                        numberOfBedrooms = 2,
                        location = LatLng(48.859520, 2.271824),
                        estateType = applicationContext.getString(R.string.apartment),
                        price = "1.000.000",
                        pointOfInterest = listOf(
                            applicationContext.getString(R.string.schools),
                            applicationContext.getString(R.string.parks),
                            applicationContext.getString(R.string.subway)
                        ),
                        sellingDate = null,
                        entryDate = LocalDate.of(2017, 11, 25),
                        status = EstateStatus.FOR_SALE,
                    ),
                    EstateEntity(
                        id = 4,
                        description = applicationContext.getString(R.string.fourth_house_desc),
                        surface = "130m²",
                        roomNumber = 11,
                        bathroomNumber = 3,
                        numberOfBedrooms = 4,
                        location = LatLng(0.0, 0.0),
                        estateType = applicationContext.getString(R.string.apartment),
                        price = "750.000",
                        pointOfInterest = listOf(
                            applicationContext.getString(R.string.schools),
                            applicationContext.getString(R.string.parks),
                            applicationContext.getString(R.string.subway)
                        ),
                        sellingDate = null,
                        entryDate = LocalDate.of(2022, 10, 11),
                        status = EstateStatus.FOR_SALE,
                    ),
                )
            )
        }
        return Result.success()
    }
}