package com.despaircorp.data.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.despaircorp.data.currency.dao.CurrencyDao
import com.despaircorp.data.currency.dto.CurrencyDto
import com.despaircorp.data.estate.dao.EstateDao
import com.despaircorp.data.estate.dto.EstateDto
import com.despaircorp.data.picture.dao.PictureDao
import com.despaircorp.data.picture.dto.PictureDto
import com.despaircorp.data.real_estate_agent.dao.RealEstateAgentDao
import com.despaircorp.data.real_estate_agent.dto.RealEstateAgentDto
import com.despaircorp.data.utils.TypeConvertinator

@Database(
    entities = [
        RealEstateAgentDto::class,
        EstateDto::class,
        PictureDto::class,
        CurrencyDto::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConvertinator::class)
public abstract class RealEstateManagerRoomDatabase : RoomDatabase() {
    abstract fun getRealEstateAgentDao(): RealEstateAgentDao
    abstract fun getEstateDao(): EstateDao
    
    abstract fun getPictureDao(): PictureDao
    
    abstract fun getCurrencyDao(): CurrencyDao
    
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: RealEstateManagerRoomDatabase? = null
        
        fun getDatabase(context: Context): RealEstateManagerRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RealEstateManagerRoomDatabase::class.java,
                    "real_estate_manager_database"
                ).build()
                
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
    
}