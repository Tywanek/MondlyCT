package com.radlab.mondlyct.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.radlab.mondlyct.models.Item

@TypeConverters(Converters::class)
@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun codeTaskDao(): CodeTaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = createDatabase(context)
                    INSTANCE = instance
                }
                return instance
            }
        }
        private fun createDatabase(applicationContext: Context): AppDatabase {
            return Room.databaseBuilder(applicationContext, AppDatabase::class.java, "items_database")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}