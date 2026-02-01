package com.l13devstudio.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.l13devstudio.data.source.local.database.dao.LikeDao
import com.l13devstudio.data.source.local.database.entity.LikeDbo

@Database(
    entities = [
        LikeDbo::class
    ], version = 1
)
abstract class CoreDatabase: RoomDatabase() {

    abstract fun likeDao(): LikeDao

    companion object{

        private var INSTANCE: CoreDatabase? = null
        private const val DB_NAME = "app_core.db"

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this){
            INSTANCE ?: run { buildInstance(context).also { INSTANCE = it } }
        }

        private fun buildInstance(context: Context) = Room.databaseBuilder(
            context, CoreDatabase::class.java, DB_NAME).build()

    }

}