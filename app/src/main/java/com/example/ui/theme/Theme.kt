package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    primaryContainer = ForestGreenContainer,
    onPrimaryContainer = OnForestGreenContainer,
    secondary = OceanTeal,
    onSecondary = Color.White,
    secondaryContainer = OceanTealContainer,
    onSecondaryContainer = OnOceanTealContainer,
    tertiary = IslandSun,
    onTertiary = Color.White,
    tertiaryContainer = IslandSunContainer,
    background = TropicalBackground,
    onBackground = Color(0xFF191C1A),
    surface = TropicalSurface,
    onSurface = Color(0xFF191C1A),
    surfaceVariant = TropicalSurfaceVariant,
    onSurfaceVariant = Color(0xFF414943),
    outline = TropicalOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = ForestGreenDark,
    onPrimary = Color(0xFF003825),
    primaryContainer = ForestGreenContainerDark,
    onPrimaryContainer = ForestGreenContainer,
    secondary = OceanTealDark,
    onSecondary = Color(0xFF00363D),
    secondaryContainer = OceanTealContainerDark,
    onSecondaryContainer = OceanTealContainer,
    tertiary = IslandSunDark,
    onTertiary = Color(0xFF412D00),
    background = DarkBackground,
    onBackground = Color(0xFFE1E3DF),
    surface = DarkSurface,
    onSurface = Color(0xFFE1E3DF),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC1C9C1),
    outline = Color(0xFF8B938C)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

