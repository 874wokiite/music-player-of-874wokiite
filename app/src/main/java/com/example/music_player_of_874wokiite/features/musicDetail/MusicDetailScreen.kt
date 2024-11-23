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
import com.example.music_player_of_874wokiite.components.DownIconButton
import com.example.music_player_of_874wokiite.components.SeekBar
import com.example.music_player_of_874wokiite.features.musicDetail.MusicViewModel

@Composable
fun MusicDetailScreen(
    coverImage: String,
    musicTitle: String,
    albumTitle: String,
    audioFile: String,
    modifier: Modifier = Modifier,
    musicViewModel: MusicViewModel,
    isPlaying: Boolean,
    onPlay: () -> Unit,  // 再生開始コールバック
    onPause: () -> Unit,  // 再生停止コールバック
    onClose: () -> Unit,  // 画面を閉じるコールバック
    onNext: () -> Unit,  // 次の曲に進めるコールバック
    onPrevious: () -> Unit,  // 前の曲に戻るコールバック
) {
    val context = LocalContext.current
    val bitmap = remember { loadBitmapFromAssets(context, coverImage) }
    val currentPosition by musicViewModel.currentPosition.observeAsState(0)
    val duration by musicViewModel.duration.observeAsState(0)

    // audioFileが変わったときのみ再生を準備・開始
    LaunchedEffect(audioFile) {
        musicViewModel.prepareAndPlay(context, audioFile)
    }

    Column(modifier = modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
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
            onValueChange = {
                musicViewModel.seekTo(it.toInt())
            }
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
