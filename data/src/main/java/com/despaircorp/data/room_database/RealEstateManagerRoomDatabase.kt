package com.despaircorp.data.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.despaircorp.data.real_estate_agent.dao.RealEstateAgentDao
import com.despaircorp.data.real_estate_agent.dto.RealEstateAgentDto

@Database(
    entities = [
        RealEstateAgentDto::class,
    ],
    version = 1,
    exportSchema = false
)
public abstract class RealEstateManagerRoomDatabase : RoomDatabase() {
    abstract fun getRealEstateAgentDao(): RealEstateAgentDao
    
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