package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.SavedIdeaDao
import com.example.data.local.dao.ScanRecordDao
import com.example.data.local.entity.SavedIdeaEntity
import com.example.data.local.entity.ScanRecordEntity

@Database(
    entities = [ScanRecordEntity::class, SavedIdeaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanRecordDao(): ScanRecordDao
    abstract fun savedIdeaDao(): SavedIdeaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "solomon_recycle_db"
                ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
