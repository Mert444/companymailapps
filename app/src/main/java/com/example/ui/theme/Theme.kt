package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ChampagneGold,
    onPrimary = ObsidianBackground,
    primaryContainer = GoldContainerLight,
    onPrimaryContainer = OnGoldContainerLight,
    secondary = CyanExecutive,
    onSecondary = ObsidianBackground,
    background = ObsidianBackground,
    onBackground = TextPrimary,
    surface = ObsidianSurface,
    onSurface = TextPrimary,
    surfaceVariant = ObsidianSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = ObsidianBorder,
    outlineVariant = GoldGlowBorder,
    error = CrimsonError
)

private val LightColorScheme = darkColorScheme( // Force rich dark theme for executive look
    primary = ChampagneGold,
    onPrimary = ObsidianBackground,
    primaryContainer = GoldContainerLight,
    onPrimaryContainer = OnGoldContainerLight,
    secondary = CyanExecutive,
    onSecondary = ObsidianBackground,
    background = ObsidianBackground,
    onBackground = TextPrimary,
    surface = ObsidianSurface,
    onSurface = TextPrimary,
    surfaceVariant = ObsidianSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = ObsidianBorder,
    outlineVariant = GoldGlowBorder,
    error = CrimsonError
)

@Composable
fun ExecTranslateTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
