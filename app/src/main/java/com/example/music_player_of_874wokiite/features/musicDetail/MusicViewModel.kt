package com.example.music_player_of_874wokiite.features.musicDetail

// 再生に必要なロジックやヘルパークラス
import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {
    private var mediaPlayer: MediaPlayer? = MediaPlayer()
    // MutableLiveDataで再生状態を管理
    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    fun prepareAndPlay(context: Context, audioFile: String) {
        viewModelScope.launch {
            val assetFileDescriptor = context.assets.openFd(audioFile)
            mediaPlayer?.apply {
                reset()
                setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
                prepare()
                start()
                _isPlaying.value = true  // 再生状態を更新
            }
            assetFileDescriptor.close()
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        _isPlaying.value = false  // 再生状態を更新
    }

    fun resume() {
        mediaPlayer?.start()
        _isPlaying.value = true  // 再生状態を更新
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
