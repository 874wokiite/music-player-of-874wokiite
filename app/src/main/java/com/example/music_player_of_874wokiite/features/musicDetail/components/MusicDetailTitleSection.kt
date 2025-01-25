package com.example.music_player_of_874wokiite.features.musicDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.music_player_of_874wokiite.modifiers.innerShadow
import com.example.music_player_of_874wokiite.ui.theme.MusicPlayerOf874wokiiteTheme

@Composable
fun MusicDetailTitleSection(
    musicTitle: String,
    albumTitle: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.inverseSurface)
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = musicTitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.inverseOnSurface,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = albumTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.inverseOnSurface,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .innerShadow(
                    shape = RectangleShape,
                    color = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.6f),
                    blur = 4.dp,
                    offsetY = 2.dp,
                    offsetX = 2.dp,
                )
        )
    }
}

@Preview
@Composable
fun PreviewMusicDetailTitleSection() {
    MusicPlayerOf874wokiiteTheme {
        MusicDetailTitleSection(
            musicTitle = "xxxDay",
            albumTitle = "xxxDay",
        )
    }
}