package com.example.music_player_of_874wokiite.model

data class SelectedMusicData(
    val coverImage: String,
    val musicTitle: String,
    val albumTitle: String,
    val audioFile: String,
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0,
    val duration: Int = 0
)