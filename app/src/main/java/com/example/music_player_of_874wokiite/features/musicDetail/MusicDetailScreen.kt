package com.example.music_player_of_874wokiite.ui

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.music_player_of_874wokiite.components.CustomButton
import com.example.music_player_of_874wokiite.components.DownIconButton
import com.example.music_player_of_874wokiite.components.SeekBar
import com.example.music_player_of_874wokiite.ui.theme.MusicPlayerOf874wokiiteTheme

@Composable
fun MusicDetailScreen(
    coverImage: String,
    musicTitle: String,
    albumTitle: String,
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onClose: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onValueChange: (Float) -> Unit,
    currentPosition: Int,
    duration: Int,
) {
    val context = LocalContext.current
    val bitmap = remember { loadBitmapFromAssets(context, coverImage) }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(0.dp))
        DownIconButton(
            onClick = { onClose() }
        )
        bitmap?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = musicTitle, style = MaterialTheme.typography.titleMedium)
        Text(text = albumTitle, style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Now Playing: $musicTitle", style = MaterialTheme.typography.bodyMedium)

        SeekBar(
            currentPosition = currentPosition,
            duration = duration,
            onValueChange = onValueChange
        )

        // 再生と停止ボタン
        CustomButton(
            modifier = modifier,
            isPlaying = isPlaying,
            onPlay = { onPlay() },
            onPause = { onPause() },
            onNext = { onNext() },
            onPrevious = { onPrevious() }
        )
    }
}

// Bitmap読み込み関数
fun loadBitmapFromAssets(context: Context, fileName: String) =
    context.assets.open(fileName).use { BitmapFactory.decodeStream(it)?.asImageBitmap() }

@Preview
@Composable
fun PreviewMusicDetailScreen(modifier: Modifier = Modifier) {
    MusicPlayerOf874wokiiteTheme {
        MusicDetailScreen(
            coverImage = "xxxDay.png",
            musicTitle = "xxxDay",
            albumTitle = "xxxDay",
            modifier = modifier,
            isPlaying = true,
            onPlay = {},
            onPause = {},
            onClose = {},
            onNext = {},
            onPrevious = {},
            onValueChange = {},
            currentPosition = 0,
            duration = 0,
        )
    }
}