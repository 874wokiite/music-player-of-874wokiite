package com.example.music_player_of_874wokiite.utils

import android.content.Context
import android.media.MediaPlayer

class MediaPlayerHandler {
    private var mediaPlayer: MediaPlayer? = null

    fun initializeMediaPlayer(context: Context): MediaPlayer {
        mediaPlayer = MediaPlayer()
        return mediaPlayer!!
    }

    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
