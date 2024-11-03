package com.example.music_player_of_874wokiite.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SeekBar(
    currentPosition: Int,
    duration: Int,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Slider(
            value = currentPosition.toFloat(),
            onValueChange = onValueChange,
            valueRange = 0f..duration.toFloat(),
            modifier = modifier.fillMaxWidth().padding(vertical = 16.dp)
        )
        // 現在の再生位置と曲の合計時間を表示
        Text(
            text = formatTime(currentPosition, duration),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@SuppressLint("DefaultLocale")
fun formatTime(currentPosition: Int, duration: Int): String {
    return String.format(
        "%02d:%02d / %02d:%02d",
        currentPosition / 1000 / 60, currentPosition / 1000 % 60,
        duration / 1000 / 60, duration / 1000 % 60
    )
}