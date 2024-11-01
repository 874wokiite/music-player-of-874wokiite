package com.example.music_player_of_874wokiite.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// 再生と停止ボタンを作成
@Composable
fun CustomButton(modifier: Modifier = Modifier, isPlaying: Boolean, onPlayClick: () -> Unit, onStopClick: () -> Unit) {
    Row {
        // 再生ボタン
        PlayButton(modifier = modifier, isPlaying = isPlaying, onClick = onPlayClick)
        // 停止ボタン
        StopButton(modifier = modifier, onClick = onStopClick)
    }
}

// 再生ボタン
@Composable
fun PlayButton(modifier: Modifier = Modifier, isPlaying: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Play"
        )
    }
}

// 停止ボタン
@Composable
fun StopButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.Filled.Pause,
            contentDescription = "Stop"
        )
    }
}
