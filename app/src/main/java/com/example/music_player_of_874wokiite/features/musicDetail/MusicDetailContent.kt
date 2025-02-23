package com.example.music_player_of_874wokiite.features.musicDetail

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.music_player_of_874wokiite.components.CustomButton
import com.example.music_player_of_874wokiite.components.DownIconButton
import com.example.music_player_of_874wokiite.components.SeekBar
import com.example.music_player_of_874wokiite.features.musicDetail.components.MusicDetailImageSection
import com.example.music_player_of_874wokiite.features.musicDetail.components.MusicDetailTitleSection
import com.example.music_player_of_874wokiite.ui.theme.MusicPlayerOf874wokiiteTheme

@Composable
fun MusicDetailContent(
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
        DownIconButton(onClick = { onClose() })
        Spacer(modifier = Modifier.height(16.dp))
        MusicDetailImageSection(bitmap = bitmap)
        Spacer(modifier = Modifier.height(16.dp))
        MusicDetailTitleSection(musicTitle = musicTitle, albumTitle = albumTitle)
        Spacer(modifier = Modifier.height(16.dp))

        SeekBar(
            currentPosition = currentPosition, duration = duration, onValueChange = onValueChange
        )

        // 再生と停止ボタン
        CustomButton(modifier = modifier,
            isPlaying = isPlaying,
            onPlay = { onPlay() },
            onPause = { onPause() },
            onNext = { onNext() },
            onPrevious = { onPrevious() })
    }
}

// Bitmap読み込み関数
fun loadBitmapFromAssets(context: Context, fileName: String) =
    context.assets.open(fileName).use { BitmapFactory.decodeStream(it)?.asImageBitmap() }

@Preview
@Composable
fun PreviewMusicDetailContent(modifier: Modifier = Modifier) {
    MusicPlayerOf874wokiiteTheme {
        MusicDetailContent(
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