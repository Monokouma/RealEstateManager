package com.despaircorp.data.estate.content_provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import com.despaircorp.data.estate.dao.EstateDao
import com.despaircorp.data.estate.dto.EstateDto
import com.despaircorp.data.room_database.RealEstateManagerRoomDatabase
import javax.inject.Inject


class EstateContentProvider @Inject constructor(

) : ContentProvider() {
    
    private lateinit var estateDao: EstateDao
    
    override fun onCreate(): Boolean {
        val context = context ?: return false
        estateDao = Room.databaseBuilder(
            context,
            RealEstateManagerRoomDatabase::class.java,
            "real_estate_manager_database"
        ).build().getEstateDao()
        
        return true
    }
    
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        return estateDao.getItemsWithCursor()
    }
    
    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            SAMPLE_DATA -> "vnd.android.cursor.dir/com.despaircorp.data.estate.dto.EstateDto"
            SAMPLE_DATA_ID -> "vnd.android.cursor.item/com.despaircorp.data.estate.dto.EstateDto"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }
    
    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val id = estateDao.insertForProvider(EstateDto.fromContentValues(values))
        context?.contentResolver?.notifyChange(uri, null)
        return ContentUris.withAppendedId(uri, id)
    }
    
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val id = ContentUris.parseId(uri)
        return estateDao.deleteDataById(id.toInt())
    }
    
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val sampleData = EstateDto.fromContentValues(values)
        return estateDao.updateEstate(sampleData)
    }
    
    companion object {
        const val AUTHORITY = "com.despaircorp.data"
        const val SAMPLE_DATA = 1
        const val SAMPLE_DATA_ID = 2
        
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "estate_table", SAMPLE_DATA)
            addURI(AUTHORITY, "estate_table/#", SAMPLE_DATA_ID)
        }
    }
    
}