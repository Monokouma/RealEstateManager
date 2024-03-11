package com.despaircorp.data.estate.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.despaircorp.data.estate.dto.EstateDto
import com.despaircorp.data.estate.dto.EstateWithPictureDto
import kotlinx.coroutines.flow.Flow

@Dao
interface EstateDao {
    @Query("SELECT EXISTS(SELECT * FROM estate_table)")
    suspend fun exist(): Boolean
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAsList(estateDtoList: List<EstateDto>)
    
    @Transaction
    @Query("SELECT * FROM estate_table")
    fun getEstateWithPictureAsFlow(): Flow<List<EstateWithPictureDto>>
}