package com.example.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Camera : Screen("camera", "Camera")
    object Gallery : Screen("gallery", "Gallery")
    object Result : Screen("result", "Analysis Result")
    object History : Screen("history", "Scan History")
    object Saved : Screen("saved", "Saved Ideas")
    object Guide : Screen("guide", "Recycling Guide")
    object Education : Screen("education", "Learn About Waste")
    object Settings : Screen("settings", "Settings")
    object Privacy : Screen("privacy", "Privacy Policy")
}
