package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_records")
data class ScanRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val objectName: String,
    val material: String,
    val category: String,
    val confidence: Float,
    val condition: String,
    val recyclable: Boolean,
    val reusable: Boolean,
    val compostable: Boolean,
    val hazardLevel: String,
    val summary: String,
    val reuseIdeasJson: String,
    val recyclingAdviceJson: String,
    val searchKeywordsJson: String,
    val imagePath: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_ideas")
data class SavedIdeaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val scanId: Long? = null,
    val objectName: String,
    val material: String,
    val ideaTitle: String,
    val ideaDescription: String,
    val difficulty: String,
    val estimatedCost: String,
    val imagePath: String? = null,
    val relatedVideoUrl: String? = null,
    val relatedVideoTitle: String? = null,
    val savedAt: Long = System.currentTimeMillis()
)
