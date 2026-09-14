package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.Screen
import com.example.ui.screens.CameraScreen
import com.example.ui.screens.EducationScreen
import com.example.ui.screens.GalleryPickerScreen
import com.example.ui.screens.GuideScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.PrivacyScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.SavedIdeasScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.RecycleViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: RecycleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val darkModePref by viewModel.darkModeEnabled.collectAsStateWithLifecycle()
            val systemDark = isSystemInDarkTheme()
            val useDark = darkModePref || systemDark

            MyApplicationTheme(darkTheme = useDark) {
                SolomonRecycleAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SolomonRecycleAppContent(viewModel: RecycleViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        Screen.Home to Icons.Default.Home,
        Screen.History to Icons.Default.History,
        Screen.Saved to Icons.Default.Bookmark,
        Screen.Guide to Icons.AutoMirrored.Filled.MenuBook,
        Screen.Settings to Icons.Default.Settings
    )

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.History.route,
        Screen.Saved.route,
        Screen.Guide.route,
        Screen.Settings.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    bottomNavItems.forEach { (screen, icon) ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            icon = { Icon(imageVector = icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = selected,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ForestGreen,
                                selectedTextColor = ForestGreen,
                                indicatorColor = ForestGreen.copy(alpha = 0.15f)
                            ),
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.testTag("nav_item_${screen.route}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToCamera = { navController.navigate(Screen.Camera.route) },
                    onNavigateToGallery = { navController.navigate(Screen.Gallery.route) },
                    onNavigateToSaved = { navController.navigate(Screen.Saved.route) },
                    onNavigateToGuide = { navController.navigate(Screen.Guide.route) },
                    onNavigateToEducation = { navController.navigate(Screen.Education.route) }
                )
            }

            composable(Screen.Camera.route) {
                CameraScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onAnalysisComplete = { navController.navigate(Screen.Result.route) }
                )
            }

            composable(Screen.Gallery.route) {
                GalleryPickerScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onAnalysisComplete = { navController.navigate(Screen.Result.route) }
                )
            }

            composable(Screen.Result.route) {
                ResultScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onScanAnother = {
                        navController.navigate(Screen.Camera.route) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }

            composable(Screen.History.route) {
                HistoryScreen(
                    viewModel = viewModel,
                    onNavigateToCamera = { navController.navigate(Screen.Camera.route) },
                    onOpenScan = { navController.navigate(Screen.Result.route) }
                )
            }

            composable(Screen.Saved.route) {
                SavedIdeasScreen(
                    viewModel = viewModel,
                    onNavigateToCamera = { navController.navigate(Screen.Camera.route) }
                )
            }

            composable(Screen.Guide.route) {
                GuideScreen(
                    viewModel = viewModel,
                    onNavigateToEducation = { navController.navigate(Screen.Education.route) }
                )
            }

            composable(Screen.Education.route) {
                EducationScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = viewModel,
                    onNavigateToPrivacy = { navController.navigate(Screen.Privacy.route) }
                )
            }

            composable(Screen.Privacy.route) {
                PrivacyScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
