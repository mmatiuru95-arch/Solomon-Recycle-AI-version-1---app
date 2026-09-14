package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.SavedIdeaEntity
import com.example.data.local.entity.ScanRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanRecordDao {
    @Query("SELECT * FROM scan_records ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM scan_records WHERE id = :id LIMIT 1")
    suspend fun getScanById(id: Long): ScanRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: ScanRecordEntity): Long

    @Query("DELETE FROM scan_records WHERE id = :id")
    suspend fun deleteScanById(id: Long)

    @Query("DELETE FROM scan_records")
    suspend fun clearAllScans()

    @Query("SELECT COUNT(*) FROM scan_records")
    fun getScansCount(): Flow<Int>
}

@Dao
interface SavedIdeaDao {
    @Query("SELECT * FROM saved_ideas ORDER BY savedAt DESC")
    fun getAllSavedIdeas(): Flow<List<SavedIdeaEntity>>

    @Query("SELECT * FROM saved_ideas WHERE id = :id LIMIT 1")
    suspend fun getSavedIdeaById(id: Long): SavedIdeaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedIdea(idea: SavedIdeaEntity): Long

    @Query("DELETE FROM saved_ideas WHERE id = :id")
    suspend fun deleteSavedIdeaById(id: Long)

    @Query("DELETE FROM saved_ideas")
    suspend fun clearAllSavedIdeas()

    @Query("SELECT COUNT(*) > 0 FROM saved_ideas WHERE ideaTitle = :title AND objectName = :objectName")
    fun isIdeaSaved(title: String, objectName: String): Flow<Boolean>

    @Query("SELECT COUNT(*) FROM saved_ideas")
    fun getSavedCount(): Flow<Int>
}
