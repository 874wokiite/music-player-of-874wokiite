package com.example.music_player_of_874wokiite.ui

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.music_player_of_874wokiite.components.CustomButton
import com.example.music_player_of_874wokiite.features.musicDetail.MusicViewModel

@Composable
fun MusicDetailScreen(
    coverImage: String,
    musicTitle: String,
    albumTitle: String,
    audioFile: String,
    modifier: Modifier = Modifier,
    musicViewModel: MusicViewModel
) {
    val context = LocalContext.current
    val bitmap = remember { loadBitmapFromAssets(context, coverImage) }
    val isPlaying by musicViewModel.isPlaying.observeAsState(false)

    // audioFileが変わったときのみ再生を準備・開始
    LaunchedEffect(audioFile) {
        musicViewModel.prepareAndPlay(context, audioFile)
    }

    Column(modifier = modifier.padding(16.dp)) {
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

        // 再生と停止ボタン
        CustomButton(
            modifier = modifier,
            isPlaying = isPlaying,
            onPlayClick = {
                if (!isPlaying) {
                    musicViewModel.resume()
                }
            },
            onStopClick = {
                if (isPlaying) {
                    musicViewModel.pause()
                }
            }
        )
    }
}

// Bitmap読み込み関数
fun loadBitmapFromAssets(context: Context, fileName: String) =
    context.assets.open(fileName).use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
