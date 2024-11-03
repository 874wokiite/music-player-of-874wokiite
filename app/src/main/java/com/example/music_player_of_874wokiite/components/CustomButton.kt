package com.example.music_player_of_874wokiite.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// 再生と停止ボタンを作成
@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 前の曲ボタン
        SkipPreviousButton(modifier = Modifier, onClick = onPreviousClick)
        // isPlayingの状態によって再生か停止ボタンを表示
        if (isPlaying) {
            PauseButton(modifier = Modifier, onClick = onPauseClick)
        } else {
            PlayButton(modifier = Modifier, isPlaying = isPlaying, onClick = onPlayClick)
        }
        // 次の曲ボタン
        SkipNextButton(modifier = Modifier, onClick = onNextClick)
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
fun PauseButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.Filled.Pause,
            contentDescription = "Pause"
        )
    }
}

@Composable
fun SkipNextButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.Filled.SkipNext,
            contentDescription = "SkipNext"
        )
    }
}

@Composable
fun SkipPreviousButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.Filled.SkipPrevious,
            contentDescription = "SkipPrevious"
        )
    }
}