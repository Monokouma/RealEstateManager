package com.despaircorp.domain.location

import com.despaircorp.domain.location.model.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationDomainRepository {
    fun getUserLocationAsFlow(): Flow<LocationEntity>
}