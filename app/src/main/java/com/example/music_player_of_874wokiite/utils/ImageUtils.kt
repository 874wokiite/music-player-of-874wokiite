package com.example.music_player_of_874wokiite.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import android.graphics.BitmapFactory
import java.io.IOException

@Composable
fun loadImageFromAssets(fileName: String): ImageBitmap? {
    val context = LocalContext.current
    return remember(fileName) {
        try {
            context.assets.open(fileName).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            null
        }
    }
}

fun loadImageBitmapFromAssets(context: Context, fileName: String): ImageBitmap? {
    return try {
        context.assets.open(fileName).use { inputStream ->
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        }
    } catch (e: IOException) {
        null
    }
}