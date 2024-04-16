package com.despaircorp.data.estate.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
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
    
    @Query("SELECT * FROM estate_table")
    fun getItemsWithCursor(): Cursor
    
    @Query("SELECT * FROM estate_table WHERE id=:estateId")
    suspend fun getEstateWithPictureDtoById(estateId: Int): EstateWithPictureDto
    
    @Query("SELECT * FROM estate_table")
    suspend fun getEstateDto(): List<EstateDto>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(estateDtoList: EstateDto)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForProvider(estateDtoList: EstateDto): Long
    
    @Update
    fun updateEstate(estateDto: EstateDto): Int
    
    @Query("DELETE FROM estate_table WHERE id=:id")
    fun deleteDataById(id: Int): Int
}