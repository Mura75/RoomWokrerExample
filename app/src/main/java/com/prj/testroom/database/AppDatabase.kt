package com.prj.testroom.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prj.testroom.BuildConfig

@Database(
    entities = [
        TodoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "app_database.db"

        fun getDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .apply { if (BuildConfig.DEBUG) fallbackToDestructiveMigration() }
                .build()
        }
    }

    abstract fun getTodoDao(): TodoDao

}