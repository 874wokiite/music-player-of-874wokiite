package com.example.music_player_of_874wokiite.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Music/Color
val ColorPrimaryLight = Color(0xFF571DAF)
val ColorPrimaryDark = Color(0xFF240257)

// Music/Object (Light & Dark)
val ObjectPrimaryLight = Color(0xFF292733)
val ObjectPrimaryDark = Color(0xFF6345E7)

val ObjectSecondaryLight = Color(0xFFF4EFF0)
val ObjectSecondaryDark = Color(0xFF340F62)

// Music/Text (Light & Dark)
val TextPrimaryLight = Color(0xFF403C48)
val TextPrimaryDark = Color(0xFF403C48)

val TextSecondaryLight = Color(0xFF838383)
val TextSecondaryDark = Color(0xFFB5B5B5)

val TextTertiaryLight = Color(0xFFF6F6F6)
val TextTertiaryDark = Color(0xFFF6F6F6)

// Music/Background (Light & Dark)
val BackgroundLight = Color(0xFFDFD9DA)
val BackgroundDark = Color(0xFF1B0536)

// Music/Gradient (Light & Dark)
val LightGradientPalette = GradientPalette(
    backgroundGradient = Brush.linearGradient(
        colors = listOf(Color.White, Color.LightGray)
    ),
    surfaceGradient = Brush.linearGradient(
        colors = listOf(Color.LightGray, Color.Gray)
    )
)

val DarkGradientPalette = GradientPalette(
    backgroundGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6345E7).copy(alpha = 0.1f),
            Color(0xFF6345E7).copy(alpha = 0f)
        ),
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    ),
    surfaceGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF838383).copy(alpha = 0f),
            Color(0xFF838383).copy(alpha = 0.1f)
        ),
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
)

data class GradientPalette(
    val backgroundGradient: Brush,
    val surfaceGradient: Brush
)