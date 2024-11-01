package com.example.music_player_of_874wokiite.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light and Dark Color Schemes
private val LightColors = lightColorScheme(
    primary = ColorPrimaryLight,
    onPrimary = ObjectPrimaryLight,
    secondary = ObjectSecondaryLight,
    onSecondary = TextPrimaryLight,
    background = ObjectTertiaryLight,
    surface = ObjectTertiaryLight,
    onBackground = TextPrimaryLight,
    onSurface = TextSecondaryLight
)

private val DarkColors = darkColorScheme(
    primary = ColorPrimaryDark,
    onPrimary = ObjectPrimaryDark,
    secondary = ObjectSecondaryDark,
    onSecondary = TextPrimaryDark,
    background = ObjectTertiaryDark,
    surface = ObjectTertiaryDark,
    onBackground = TextPrimaryDark,
    onSurface = TextSecondaryDark
)

@Composable
fun MusicPlayerOf874wokiiteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}