package com.example.music_palyer_of_874wokiite.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.music_palyer_of_874wokiite.R

@Composable
fun MusicDetailScreen(
    coverImage: String,
    musicTitle: String,
    albumTitle: String,
    audioFile: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bitmap = remember { loadBitmapFromAssets(context, coverImage) }

    // MediaPlayerを使って音声再生
    val mediaPlayer = remember { MediaPlayer() }

    LaunchedEffect(audioFile) {
        // assetsからmp3ファイルを開いて再生
        context.assets.openFd(audioFile).use { assetFileDescriptor ->
            mediaPlayer.apply {
                reset()
                setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
                prepare()
                start() // 再生を開始
            }
        }

//        onDispose {
//            // クリーンアップ
//            mediaPlayer.release()
//        }
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
        Text(text = musicTitle, color = colorResource(id = R.color.textPrimary))
        Text(text = albumTitle, color = colorResource(id = R.color.textSecondary))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Now Playing: $musicTitle")


    }
}

// Bitmap読み込み関数
fun loadBitmapFromAssets(context: Context, fileName: String) =
    context.assets.open(fileName).use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
