package com.example.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.data.local.AppDatabase
import com.example.data.local.OfflineKnowledge
import com.example.data.local.entity.SavedIdeaEntity
import com.example.data.local.entity.ScanRecordEntity
import com.example.data.model.AnalysisResult
import com.example.data.model.EnvironmentalTip
import com.example.data.model.ImpactStats
import com.example.data.model.RecyclingGuideItem
import com.example.data.model.ReuseIdea
import com.example.data.model.YouTubeVideo
import com.example.data.remote.GeminiService
import com.example.data.remote.YouTubeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class RecycleRepository(
    private val context: Context,
    private val database: AppDatabase,
    private val geminiService: GeminiService = GeminiService(),
    private val youtubeService: YouTubeService = YouTubeService()
) {
    private val prefs = context.getSharedPreferences("solomon_recycle_prefs", Context.MODE_PRIVATE)

    private val _dataSaverEnabled = MutableStateFlow(prefs.getBoolean(KEY_DATA_SAVER, false))
    val dataSaverEnabled = _dataSaverEnabled.asStateFlow()

    private val _darkModeEnabled = MutableStateFlow(prefs.getBoolean(KEY_DARK_MODE, false))
    val darkModeEnabled = _darkModeEnabled.asStateFlow()

    private val _language = MutableStateFlow(prefs.getString(KEY_LANGUAGE, "en") ?: "en")
    val language = _language.asStateFlow()

    fun setDataSaver(enabled: Boolean) {
        _dataSaverEnabled.value = enabled
        prefs.edit().putBoolean(KEY_DATA_SAVER, enabled).apply()
    }

    fun setDarkMode(enabled: Boolean) {
        _darkModeEnabled.value = enabled
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    fun setLanguage(lang: String) {
        _language.value = lang
        prefs.edit().putString(KEY_LANGUAGE, lang).apply()
    }

    // Scans
    fun getAllScans(): Flow<List<ScanRecordEntity>> = database.scanRecordDao().getAllScans()

    suspend fun getScanById(id: Long): ScanRecordEntity? = database.scanRecordDao().getScanById(id)

    suspend fun saveScan(
        result: AnalysisResult,
        bitmap: Bitmap?
    ): Long = withContext(Dispatchers.IO) {
        var imagePath: String? = null
        if (bitmap != null) {
            try {
                val filename = "scan_${System.currentTimeMillis()}.jpg"
                val file = File(context.filesDir, filename)
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
                }
                imagePath = file.absolutePath
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save image locally", e)
            }
        }

        val reuseJson = JSONArray().apply {
            result.reuseIdeas.forEach { idea ->
                put(org.json.JSONObject().apply {
                    put("title", idea.title)
                    put("description", idea.description)
                    put("difficulty", idea.difficulty)
                    put("estimatedCost", idea.estimatedCost)
                })
            }
        }.toString()

        val adviceJson = JSONArray(result.recyclingAdvice).toString()
        val keywordsJson = JSONArray(result.searchKeywords).toString()

        val entity = ScanRecordEntity(
            objectName = result.objectName,
            material = result.material,
            category = result.category,
            confidence = result.confidence,
            condition = result.condition,
            recyclable = result.recyclable,
            reusable = result.reusable,
            compostable = result.compostable,
            hazardLevel = result.hazardLevel,
            summary = result.summary,
            reuseIdeasJson = reuseJson,
            recyclingAdviceJson = adviceJson,
            searchKeywordsJson = keywordsJson,
            imagePath = imagePath
        )
        database.scanRecordDao().insertScan(entity)
    }

    suspend fun deleteScan(id: Long) = database.scanRecordDao().deleteScanById(id)

    suspend fun clearHistory() = database.scanRecordDao().clearAllScans()

    // Saved Ideas
    fun getAllSavedIdeas(): Flow<List<SavedIdeaEntity>> = database.savedIdeaDao().getAllSavedIdeas()

    fun isIdeaSaved(title: String, objectName: String): Flow<Boolean> =
        database.savedIdeaDao().isIdeaSaved(title, objectName)

    suspend fun saveIdea(
        objectName: String,
        material: String,
        idea: ReuseIdea,
        imagePath: String? = null,
        relatedVideoUrl: String? = null,
        relatedVideoTitle: String? = null
    ): Long = withContext(Dispatchers.IO) {
        val entity = SavedIdeaEntity(
            objectName = objectName,
            material = material,
            ideaTitle = idea.title,
            ideaDescription = idea.description,
            difficulty = idea.difficulty,
            estimatedCost = idea.estimatedCost,
            imagePath = imagePath,
            relatedVideoUrl = relatedVideoUrl,
            relatedVideoTitle = relatedVideoTitle
        )
        database.savedIdeaDao().insertSavedIdea(entity)
    }

    suspend fun deleteSavedIdea(id: Long) = database.savedIdeaDao().deleteSavedIdeaById(id)

    suspend fun clearSavedIdeas() = database.savedIdeaDao().clearAllSavedIdeas()

    // Analysis
    suspend fun analyzeWasteItem(
        bitmap: Bitmap,
        apiKeyOverride: String? = null
    ): Result<AnalysisResult> {
        val isDataSaver = _dataSaverEnabled.value
        return geminiService.analyzeImage(
            bitmap = bitmap,
            dataSaverEnabled = isDataSaver,
            apiKeyOverride = apiKeyOverride
        )
    }

    // YouTube
    suspend fun searchYouTubeVideos(
        query: String,
        apiKeyOverride: String? = null
    ): Result<List<YouTubeVideo>> {
        val maxResults = if (_dataSaverEnabled.value) 5 else 8
        return youtubeService.searchVideos(
            query = query,
            maxResults = maxResults,
            apiKeyOverride = apiKeyOverride
        )
    }

    // Guides & Search
    fun getRecyclingGuides(): List<RecyclingGuideItem> = OfflineKnowledge.recyclingGuides

    fun searchKnowledge(query: String): List<RecyclingGuideItem> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return OfflineKnowledge.recyclingGuides

        return OfflineKnowledge.recyclingGuides.filter { item ->
            item.name.lowercase().contains(q) ||
                    item.category.lowercase().contains(q) ||
                    item.material.lowercase().contains(q) ||
                    item.description.lowercase().contains(q) ||
                    item.reuseTips.any { it.lowercase().contains(q) } ||
                    item.howToRecycle.any { it.lowercase().contains(q) }
        }
    }

    fun getTodayEnvironmentalTip(): EnvironmentalTip {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val tips = OfflineKnowledge.environmentalTips
        val index = (dayOfYear % tips.size).coerceAtLeast(0)
        return tips[index]
    }

    // Environmental Impact Stats
    val impactStats: Flow<ImpactStats> = combine(
        database.scanRecordDao().getAllScans(),
        database.savedIdeaDao().getAllSavedIdeas()
    ) { scans, saved ->
        val scanned = scans.size
        val reusable = scans.count { it.reusable }
        val recyclable = scans.count { it.recyclable }
        val savedCount = saved.size
        ImpactStats(
            itemsScanned = scanned,
            itemsReusable = reusable,
            itemsRecyclable = recyclable,
            itemsSaved = savedCount
        )
    }

    companion object {
        private const val TAG = "RecycleRepository"
        private const val KEY_DATA_SAVER = "data_saver_mode"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_LANGUAGE = "language"
    }
}
