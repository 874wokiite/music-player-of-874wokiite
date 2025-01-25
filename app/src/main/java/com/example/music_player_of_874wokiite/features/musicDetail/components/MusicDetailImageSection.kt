package com.example.music_player_of_874wokiite.features.musicDetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.music_player_of_874wokiite.R
import com.example.music_player_of_874wokiite.modifiers.innerShadow
import com.example.music_player_of_874wokiite.ui.theme.MusicPlayerOf874wokiiteTheme

@Composable
fun MusicDetailImageSection(
    bitmap: ImageBitmap?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.inverseSurface)
                .padding(16.dp)
        ) {
            bitmap?.let { imageBitmap ->
                Image(
                    bitmap = imageBitmap,
                    contentDescription = null,
                    modifier = Modifier
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .innerShadow(
                        shape = RectangleShape,
                        color = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.25f),
                        blur = 4.dp,
                        offsetY = 2.dp,
                        offsetX = 2.dp,
                    )
            )
        }
    }
}

@Preview
@Composable
fun PreviewMusicDetailImageSection() {
    MusicPlayerOf874wokiiteTheme {
        MusicDetailImageSection(bitmap = null)
    }
}