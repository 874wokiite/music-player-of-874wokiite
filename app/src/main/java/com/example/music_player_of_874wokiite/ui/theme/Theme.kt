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
    inverseSurface = ObjectPrimaryLight,
    surface = ObjectSecondaryLight,
    onBackground = TextPrimaryLight,
    onSurface = TextSecondaryLight,
    inverseOnSurface = TextTertiaryLight,
    background = BackgroundLight,
)

private val DarkColors = darkColorScheme(
    primary = ColorPrimaryDark,
    inverseSurface = ObjectPrimaryDark,
    surface = ObjectSecondaryDark,
    onBackground = TextPrimaryDark,
    onSurface = TextSecondaryDark,
    inverseOnSurface = TextTertiaryDark,
    background = BackgroundDark,
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