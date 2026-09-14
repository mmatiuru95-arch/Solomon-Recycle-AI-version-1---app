package com.example.data.model

data class AnalysisResult(
    val objectName: String,
    val material: String,
    val category: String,
    val confidence: Float,
    val condition: String,
    val recyclable: Boolean,
    val reusable: Boolean,
    val compostable: Boolean,
    val hazardLevel: String, // "Low", "Medium", "High"
    val summary: String,
    val reuseIdeas: List<ReuseIdea>,
    val recyclingAdvice: List<String>,
    val searchKeywords: List<String>,
    val isLowConfidence: Boolean = false,
    val imagePath: String? = null
)

data class ReuseIdea(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val difficulty: String = "Easy", // Easy, Medium, Challenging
    val estimatedCost: String = "Free" // Free, Very Low Cost, Low Cost, Medium Cost
)

data class YouTubeVideo(
    val id: String,
    val title: String,
    val channelTitle: String,
    val description: String,
    val thumbnailUrl: String,
    val publishedAt: String = ""
)

data class RecyclingGuideItem(
    val id: String,
    val name: String,
    val category: String,
    val material: String,
    val description: String,
    val howToRecycle: List<String>,
    val reuseTips: List<String>,
    val safetyWarning: String? = null,
    val solomonContext: String
)

data class EnvironmentalTip(
    val id: Int,
    val title: String,
    val content: String,
    val category: String
)

data class EducationTopic(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: String,
    val content: String,
    val keyTakeaways: List<String>
)

data class ImpactStats(
    val itemsScanned: Int = 0,
    val itemsReusable: Int = 0,
    val itemsRecyclable: Int = 0,
    val itemsSaved: Int = 0
)
