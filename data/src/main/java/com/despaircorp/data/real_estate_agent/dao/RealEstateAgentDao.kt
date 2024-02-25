package com.despaircorp.data.real_estate_agent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.despaircorp.data.real_estate_agent.dto.RealEstateAgentDto
import kotlinx.coroutines.flow.Flow

@Dao
interface RealEstateAgentDao {
    
    @Query("SELECT EXISTS(SELECT * FROM real_estate_agent)")
    suspend fun exist(): Boolean
    
    @Query("SELECT * FROM real_estate_agent")
    suspend fun getAllRealEstateAgentDto(): List<RealEstateAgentDto>
    
    @Query("SELECT * FROM real_estate_agent")
    fun getAllRealEstateAgentDtoAsFlow(): Flow<List<RealEstateAgentDto>>
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(realEstateAgentEntities: List<RealEstateAgentDto>)
    
    @Query("SELECT * FROM real_estate_agent WHERE isLoggedIn = 1")
    suspend fun getLoggedRealEstateAgentEntity(): RealEstateAgentDto
    
    @Query("UPDATE real_estate_agent SET isLoggedIn = 1 WHERE id=:agentId")
    suspend fun logChosenAgent(agentId: Int): Int
    
    @Query("UPDATE real_estate_agent SET isLoggedIn = 0 WHERE id=:agentId")
    suspend fun disconnect(agentId: Int): Int
    
    @Query("SELECT EXISTS(SELECT * FROM real_estate_agent WHERE isLoggedIn = 1)")
    suspend fun isAgentLoggedOn(): Boolean
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOneAgent(realEstateAgentDto: RealEstateAgentDto): Long
}