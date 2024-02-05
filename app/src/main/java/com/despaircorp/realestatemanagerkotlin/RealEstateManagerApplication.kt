package com.despaircorp.realestatemanagerkotlin

import android.app.Application
import com.despaircorp.domain.real_estate_agent.RealEstateAgentDomainRepository
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.ui.utils.randomProfilePicturinator
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

@HiltAndroidApp
class RealEstateManagerApplication : Application() {
    @Inject
    lateinit var realEstateAgentDomainRepository: RealEstateAgentDomainRepository
    
    private val appScope = CoroutineScope(EmptyCoroutineContext)
    
    
    override fun onCreate() {
        super.onCreate()
        appScope.launch {
            if (!realEstateAgentDomainRepository.isRealEstateAgentTableExist()) {
                realEstateAgentDomainRepository.insertRealEstateAgentEntities(
                    listOf(
                        RealEstateAgentEntity(
                            name = "Darius",
                            id = 1,
                            imageResource = randomProfilePicturinator()
                        ),
                        RealEstateAgentEntity(
                            name = "Cassio",
                            id = 2,
                            imageResource = randomProfilePicturinator()
                        ),
                        RealEstateAgentEntity(
                            name = "Lucian",
                            id = 3,
                            imageResource = randomProfilePicturinator()
                        ),
                        RealEstateAgentEntity(
                            name = "Bard",
                            id = 4,
                            imageResource = randomProfilePicturinator()
                        ),
                        RealEstateAgentEntity(
                            name = "Nilah",
                            id = 5,
                            imageResource = randomProfilePicturinator()
                        )
                    )
                )
            }
        }
        
    }
}