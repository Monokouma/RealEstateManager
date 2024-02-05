package com.despaircorp.data.real_estate_agent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.despaircorp.data.real_estate_agent.dto.RealEstateAgentDto

@Dao
interface RealEstateAgentDao {
    
    @Query("SELECT EXISTS(SELECT * FROM real_estate_agent)")
    suspend fun exist(): Boolean
    
    @Query("SELECT * FROM real_estate_agent")
    suspend fun getAllRealEstateAgentDto(): List<RealEstateAgentDto>
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(realEstateAgentEntities: List<RealEstateAgentDto>)
}