package com.despaircorp.data.picture.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.despaircorp.data.picture.dto.PictureDto

@Dao
interface PictureDao {
    @Query("SELECT EXISTS(SELECT * FROM picture_table)")
    suspend fun exist(): Boolean
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAsList(entities: List<PictureDto>)
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: PictureDto)
    
    @Query("SELECT * FROM picture_table")
    suspend fun getAsList(): List<PictureDto>
    
    @Query("DELETE FROM picture_table WHERE estateId=:id AND imagePath=:path")
    suspend fun delete(id: Int, path: String)
}
