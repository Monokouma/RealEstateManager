package com.despaircorp.domain.estate

interface EstateDomainRepository {
    suspend fun isTableExisting(): Boolean
    fun enqueueEstateWorker()
}