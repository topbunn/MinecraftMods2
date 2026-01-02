package com.hamit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hamit.data.database.dao.LikeDao
import com.hamit.data.database.entity.LikeEntity

@Database(
    entities = [
        LikeEntity::class
    ], version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun recordAccess(): LikeDao

    companion object{

        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "internal_core.db"

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this){
            INSTANCE ?: run { buildInstance(context).also { INSTANCE = it } }
        }

        private fun buildInstance(context: Context) = Room.databaseBuilder(
            context, AppDatabase::class.java, DB_NAME).build()

    }

}