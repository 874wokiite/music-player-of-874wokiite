package com.example.music_player_of_874wokiite.ui

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.music_player_of_874wokiite.features.musicDetail.MusicDetailContent
import com.example.music_player_of_874wokiite.features.musicDetail.MusicViewModel
import com.example.music_player_of_874wokiite.model.MusicData

@Composable
fun MusicDetailScreen(
    modifier: Modifier = Modifier,
    musicData: MusicData,
    musicViewModel: MusicViewModel,
    navController: NavController
) {
    MusicDetailContent(
        coverImage = musicData.coverImage,
        musicTitle = musicData.musicTitle,
        albumTitle = musicData.albumTitle,
        modifier = modifier,
        isPlaying = musicViewModel.isPlaying.value ?: false,
        onPlay = { musicViewModel.onPlay() },
        onPause = { musicViewModel.onPause() },
        onClose = { musicViewModel.onClose(navController) },
        onNext = { musicViewModel.onNext(navController) },
        onPrevious = { musicViewModel.onPrevious(navController) },
        onValueChange = { musicViewModel.onValueChange(it.toInt()) },
        currentPosition =
        musicViewModel.currentPosition.observeAsState(0).value,
        duration = musicViewModel.duration.observeAsState(0).value,
    )
}