package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CategoryBadgesRow
import com.example.ui.components.ConfidenceIndicator
import com.example.ui.components.ReuseIdeaCard
import com.example.ui.components.YouTubeVideoCard
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.HazardRed
import com.example.ui.theme.OceanTeal
import com.example.ui.viewmodel.RecycleViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    viewModel: RecycleViewModel,
    onNavigateBack: () -> Unit,
    onScanAnother: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val analysis by viewModel.currentAnalysis.collectAsStateWithLifecycle()
    val bitmap by viewModel.currentBitmap.collectAsStateWithLifecycle()
    val videos by viewModel.youtubeVideos.collectAsStateWithLifecycle()
    val isSearchingVideos by viewModel.isSearchingVideos.collectAsStateWithLifecycle()
    val videoError by viewModel.videoSearchError.collectAsStateWithLifecycle()
    val savedIdeas by viewModel.savedIdeas.collectAsStateWithLifecycle()

    if (analysis == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "No analysis result found.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onNavigateBack) {
                    Text("Go Back")
                }
            }
        }
        return
    }

    val result = analysis!!

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Recycling Analysis", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "Recycling Ideas for ${result.objectName} (Solomon Recycle AI)"
                                )
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Identified: ${result.objectName} (${result.material})\n\n" +
                                            "Summary: ${result.summary}\n\n" +
                                            "Ideas:\n" + result.reuseIdeas.joinToString("\n") { "• ${it.title}: ${it.description} (${it.estimatedCost})" } +
                                            "\n\nChecked with Solomon Recycle AI"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Recycling Ideas"))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("result_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Scanned Image and Identification Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap!!.asImageBitmap(),
                                    contentDescription = result.objectName,
                                    modifier = Modifier
                                        .size(90.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = ForestGreen.copy(alpha = 0.15f),
                                    modifier = Modifier.size(90.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Recycling,
                                            contentDescription = null,
                                            tint = ForestGreen,
                                            modifier = Modifier.size(44.dp)
                                        )
                                    }
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "OBJECT IDENTIFIED",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = result.objectName,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Material: ${result.material}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Category: ${result.category} • Condition: ${result.condition}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Confidence Indicator
                        ConfidenceIndicator(
                            confidence = result.confidence,
                            isLowConfidence = result.isLowConfidence
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Status Badges
                        CategoryBadgesRow(
                            recyclable = result.recyclable,
                            reusable = result.reusable,
                            compostable = result.compostable,
                            hazardLevel = result.hazardLevel
                        )
                    }
                }
            }

            // Hazard Warning (if applicable)
            if (result.hazardLevel.equals("High", ignoreCase = true) || result.hazardLevel.equals("Medium", ignoreCase = true)) {
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = HazardRed.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Hazard",
                                tint = HazardRed,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Safety Notice: Potential Hazard",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = HazardRed
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Never burn or disassemble this item. Hazardous materials should be sealed safely and handled by local health or environmental authorities.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = HazardRed
                                )
                            }
                        }
                    }
                }
            }

            // Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = ForestGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "AI Summary",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = result.summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // Safe Recycling & Handling Advice
            if (result.recyclingAdvice.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = ForestGreen.copy(alpha = 0.08f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = ForestGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Solomon Islands Handling Advice",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreen
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            result.recyclingAdvice.forEach { advice ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "•",
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreen,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = advice,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Practical Reuse Ideas Section
            item {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TipsAndUpdates,
                            contentDescription = null,
                            tint = OceanTeal,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "Practical Reuse & Upcycling Ideas",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Practical low-cost projects tailored for island households",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(result.reuseIdeas) { idea ->
                val isSaved = savedIdeas.any {
                    it.ideaTitle == idea.title && it.objectName == result.objectName
                }

                ReuseIdeaCard(
                    idea = idea,
                    isSaved = isSaved,
                    onSaveClick = {
                        viewModel.saveReuseIdea(idea) { success ->
                            scope.launch {
                                if (success) {
                                    snackbarHostState.showSnackbar("Saved \"${idea.title}\" to your ideas!")
                                } else {
                                    snackbarHostState.showSnackbar("Failed to save idea.")
                                }
                            }
                        }
                    },
                    onWatchClick = {
                        viewModel.fetchYouTubeVideos("${result.objectName} ${idea.title}")
                        scope.launch {
                            snackbarHostState.showSnackbar("Searching video tutorials for ${idea.title}...")
                        }
                    }
                )
            }

            // YouTube Video Tutorials Section
            item {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            tint = Color(0xFFFF0000),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "DIY Video Tutorials",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "YouTube results are provided for educational purposes.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (isSearchingVideos) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = ForestGreen)
                    }
                }
            } else if (videoError != null) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = videoError ?: "We identified your item, but YouTube results are temporarily unavailable.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            } else if (videos.isNotEmpty()) {
                items(videos) { video ->
                    YouTubeVideoCard(video = video)
                }
            }

            // Scan Another Item Primary Button
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onScanAnother,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("scan_another_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SCAN ANOTHER ITEM",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
