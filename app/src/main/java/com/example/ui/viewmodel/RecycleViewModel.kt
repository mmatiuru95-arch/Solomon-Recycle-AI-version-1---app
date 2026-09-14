package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.SolomonRecycleApp
import com.example.data.local.entity.SavedIdeaEntity
import com.example.data.local.entity.ScanRecordEntity
import com.example.data.model.AnalysisResult
import com.example.data.model.EnvironmentalTip
import com.example.data.model.ImpactStats
import com.example.data.model.RecyclingGuideItem
import com.example.data.model.ReuseIdea
import com.example.data.model.YouTubeVideo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.InputStream

class RecycleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as SolomonRecycleApp).repository

    val dataSaverEnabled: StateFlow<Boolean> = repository.dataSaverEnabled
    val darkModeEnabled: StateFlow<Boolean> = repository.darkModeEnabled
    val language: StateFlow<String> = repository.language

    val allScans: StateFlow<List<ScanRecordEntity>> = repository.getAllScans()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedIdeas: StateFlow<List<SavedIdeaEntity>> = repository.getAllSavedIdeas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val impactStats: StateFlow<ImpactStats> = repository.impactStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ImpactStats())

    private val _currentAnalysis = MutableStateFlow<AnalysisResult?>(null)
    val currentAnalysis: StateFlow<AnalysisResult?> = _currentAnalysis.asStateFlow()

    private val _currentBitmap = MutableStateFlow<Bitmap?>(null)
    val currentBitmap: StateFlow<Bitmap?> = _currentBitmap.asStateFlow()

    private val _youtubeVideos = MutableStateFlow<List<YouTubeVideo>>(emptyList())
    val youtubeVideos: StateFlow<List<YouTubeVideo>> = _youtubeVideos.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _analysisStepText = MutableStateFlow("AI is identifying your item...")
    val analysisStepText: StateFlow<String> = _analysisStepText.asStateFlow()

    private val _analysisError = MutableStateFlow<String?>(null)
    val analysisError: StateFlow<String?> = _analysisError.asStateFlow()

    private val _isSearchingVideos = MutableStateFlow(false)
    val isSearchingVideos: StateFlow<Boolean> = _isSearchingVideos.asStateFlow()

    private val _videoSearchError = MutableStateFlow<String?>(null)
    val videoSearchError: StateFlow<String?> = _videoSearchError.asStateFlow()

    private val _todayTip = MutableStateFlow(repository.getTodayEnvironmentalTip())
    val todayTip: StateFlow<EnvironmentalTip> = _todayTip.asStateFlow()

    private val _guideSearchQuery = MutableStateFlow("")
    val guideSearchQuery: StateFlow<String> = _guideSearchQuery.asStateFlow()

    private val _filteredGuides = MutableStateFlow(repository.getRecyclingGuides())
    val filteredGuides: StateFlow<List<RecyclingGuideItem>> = _filteredGuides.asStateFlow()

    private val _savedIdeasSearchQuery = MutableStateFlow("")
    val savedIdeasSearchQuery: StateFlow<String> = _savedIdeasSearchQuery.asStateFlow()

    private val _isOffline = MutableStateFlow(!isNetworkAvailable())
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    fun checkNetworkStatus() {
        _isOffline.value = !isNetworkAvailable()
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun setImageForAnalysis(bitmap: Bitmap) {
        _currentBitmap.value = bitmap
        _analysisError.value = null
    }

    fun setImageUriForAnalysis(uri: Uri) {
        try {
            val contentResolver = getApplication<Application>().contentResolver
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            if (bitmap != null) {
                _currentBitmap.value = bitmap
                _analysisError.value = null
            } else {
                _analysisError.value = "Unable to load image. Please select another photo."
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error decoding image URI", e)
            _analysisError.value = "Failed to load selected image: ${e.localizedMessage}"
        }
    }

    fun startAnalysis(onSuccess: () -> Unit) {
        val bitmap = _currentBitmap.value
        if (bitmap == null) {
            _analysisError.value = "No image selected. Please take or pick a photo."
            return
        }

        checkNetworkStatus()
        _isAnalyzing.value = true
        _analysisError.value = null

        viewModelScope.launch {
            try {
                _analysisStepText.value = "AI is identifying your item..."
                delay(700)

                _analysisStepText.value = "Checking reuse possibilities..."
                delay(600)

                _analysisStepText.value = "Finding recycling ideas..."
                val result = repository.analyzeWasteItem(bitmap)

                if (result.isSuccess) {
                    val analysis = result.getOrThrow()
                    _currentAnalysis.value = analysis

                    // Save to Room
                    repository.saveScan(analysis, bitmap)

                    _analysisStepText.value = "Finding helpful videos..."
                    delay(500)

                    fetchYouTubeVideos(analysis.searchKeywords.firstOrNull() ?: analysis.objectName)
                    _isAnalyzing.value = false
                    onSuccess()
                } else {
                    _isAnalyzing.value = false
                    _analysisError.value = "We couldn't analyze this image. Please check your internet connection and try again."
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during analysis execution", e)
                _isAnalyzing.value = false
                _analysisError.value = "The image is difficult to identify. Try taking a clearer photo with better lighting."
            }
        }
    }

    fun fetchYouTubeVideos(query: String) {
        _isSearchingVideos.value = true
        _videoSearchError.value = null
        viewModelScope.launch {
            val result = repository.searchYouTubeVideos(query)
            _isSearchingVideos.value = false
            if (result.isSuccess) {
                _youtubeVideos.value = result.getOrThrow()
            } else {
                _videoSearchError.value = "We identified your item, but YouTube results are temporarily unavailable."
            }
        }
    }

    fun saveReuseIdea(
        idea: ReuseIdea,
        relatedVideo: YouTubeVideo? = null,
        onComplete: (Boolean) -> Unit
    ) {
        val analysis = _currentAnalysis.value ?: return
        viewModelScope.launch {
            try {
                repository.saveIdea(
                    objectName = analysis.objectName,
                    material = analysis.material,
                    idea = idea,
                    imagePath = analysis.imagePath,
                    relatedVideoUrl = relatedVideo?.let { "https://www.youtube.com/watch?v=${it.id}" },
                    relatedVideoTitle = relatedVideo?.title
                )
                onComplete(true)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save reuse idea", e)
                onComplete(false)
            }
        }
    }

    fun deleteSavedIdea(id: Long) {
        viewModelScope.launch {
            repository.deleteSavedIdea(id)
        }
    }

    fun clearSavedIdeas() {
        viewModelScope.launch {
            repository.clearSavedIdeas()
        }
    }

    fun deleteScan(id: Long) {
        viewModelScope.launch {
            repository.deleteScan(id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun onGuideSearchQueryChange(query: String) {
        _guideSearchQuery.value = query
        _filteredGuides.value = repository.searchKnowledge(query)
    }

    fun onSavedIdeasSearchQueryChange(query: String) {
        _savedIdeasSearchQuery.value = query
    }

    fun setDataSaver(enabled: Boolean) {
        repository.setDataSaver(enabled)
    }

    fun setDarkMode(enabled: Boolean) {
        repository.setDarkMode(enabled)
    }

    fun setLanguage(lang: String) {
        repository.setLanguage(lang)
    }

    fun loadScanDetails(scan: ScanRecordEntity, onLoaded: () -> Unit) {
        viewModelScope.launch {
            val reuseIdeas = mutableListOf<ReuseIdea>()
            try {
                val array = JSONArray(scan.reuseIdeasJson)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    reuseIdeas.add(
                        ReuseIdea(
                            title = obj.optString("title", "Reuse Idea"),
                            description = obj.optString("description", ""),
                            difficulty = obj.optString("difficulty", "Easy"),
                            estimatedCost = obj.optString("estimatedCost", "Low Cost")
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse reuse ideas JSON", e)
            }

            val recyclingAdvice = mutableListOf<String>()
            try {
                val array = JSONArray(scan.recyclingAdviceJson)
                for (i in 0 until array.length()) {
                    recyclingAdvice.add(array.getString(i))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse recycling advice JSON", e)
            }

            val searchKeywords = mutableListOf<String>()
            try {
                val array = JSONArray(scan.searchKeywordsJson)
                for (i in 0 until array.length()) {
                    searchKeywords.add(array.getString(i))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse search keywords JSON", e)
            }

            val bitmap = scan.imagePath?.let { path ->
                val file = File(path)
                if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
            }
            _currentBitmap.value = bitmap

            val analysis = AnalysisResult(
                objectName = scan.objectName,
                material = scan.material,
                category = scan.category,
                confidence = scan.confidence,
                condition = scan.condition,
                recyclable = scan.recyclable,
                reusable = scan.reusable,
                compostable = scan.compostable,
                hazardLevel = scan.hazardLevel,
                summary = scan.summary,
                reuseIdeas = reuseIdeas,
                recyclingAdvice = recyclingAdvice,
                searchKeywords = searchKeywords,
                isLowConfidence = scan.confidence < 0.65f,
                imagePath = scan.imagePath
            )
            _currentAnalysis.value = analysis
            fetchYouTubeVideos(searchKeywords.firstOrNull() ?: scan.objectName)
            onLoaded()
        }
    }

    companion object {
        private const val TAG = "RecycleViewModel"
    }
}
