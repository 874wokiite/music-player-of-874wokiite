package com.example.music_player_of_874wokiite.features.musicDetail

// 再生に必要なロジックなど
import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.music_player_of_874wokiite.features.musiclist.musicList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {
    private var mediaPlayer: MediaPlayer? = MediaPlayer()
    // MutableLiveDataで再生状態を管理
    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _currentPosition = MutableLiveData(0)
    val currentPosition: LiveData<Int> = _currentPosition

    private val _duration = MutableLiveData(0)
    val duration: LiveData<Int> = _duration

    private val trackList = musicList.map { it.audioFile } // 仮のトラックリスト
    private var currentTrackIndex = 0

    init {
        mediaPlayer?.setOnPreparedListener {
            _duration.value = it.duration
            it.start()
            _isPlaying.value = true
        }
        mediaPlayer?.setOnCompletionListener {
            _isPlaying.value = false
        }
    }

    fun prepareAndPlay(context: Context, audioFile: String) {
        viewModelScope.launch {
            val assetFileDescriptor = context.assets.openFd(audioFile)
            mediaPlayer?.apply {
                reset()
                setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
                prepareAsync()
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

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun nextTrack() {
        currentTrackIndex = (currentTrackIndex + 1) % trackList.size
        val nextMusic = musicList[currentTrackIndex]
        prepareAndPlay(getApplication(), nextMusic.audioFile)
    }

    fun previousTrack() {
        currentTrackIndex = if (currentTrackIndex - 1 < 0) trackList.size - 1 else currentTrackIndex - 1
        val previousMusic = musicList[currentTrackIndex]
        prepareAndPlay(getApplication(), previousMusic.audioFile)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    init {
        viewModelScope.launch {
            while (true) {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        _currentPosition.postValue(it.currentPosition)
                    }
                }
                delay(50)
            }
        }
    }
}
