package com.example.music_player_of_874wokiite.features.musicDetail

// 再生に必要なロジックなど
import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.music_player_of_874wokiite.features.musiclist.MusicData
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

    private var currentMusicTitle: String? = null
    private var currentAlbumTitle: String? = null

    init {
        mediaPlayer?.setOnPreparedListener {
            _duration.value = it.duration
            it.start()
            _isPlaying.value = true
            Log.d("MusicViewModel", "Playback started for: $currentMusicTitle")
        }
        mediaPlayer?.setOnCompletionListener {
            _isPlaying.value = false
        }
    }

    fun prepareAndPlay(context: Context, audioFile: String) {
        Log.d("MusicViewModel", "Preparing to play: $audioFile")
        viewModelScope.launch {
            try {
                context.assets.openFd(audioFile).use { assetFileDescriptor ->
                    mediaPlayer?.apply {
                        reset()
                        setDataSource(
                            assetFileDescriptor.fileDescriptor,
                            assetFileDescriptor.startOffset,
                            assetFileDescriptor.length
                        )
                        prepareAsync()
                    }
                }
            } catch (e: Exception) {
                Log.e("MusicViewModel", "Error preparing audio file: $audioFile", e)
            }
        }
    }

    fun onPause() {
        mediaPlayer?.pause()
        _isPlaying.value = false  // 再生状態を更新
    }

    fun onPlay() {
        mediaPlayer?.start()
        _isPlaying.value = true  // 再生状態を更新
    }

    fun onRefreshPlay(navController: NavController, musicData: MusicData) {
        // 現在再生中の曲と選択された曲が同じ場合
        if (currentMusicTitle == musicData.musicTitle && currentAlbumTitle == musicData.albumTitle) {
            // 画面遷移のみ
            navController.navigate("detail/${musicData.musicTitle}/${musicData.albumTitle}")
        } else {
            // 再生中の曲を変更してリフレッシュ再生
            currentMusicTitle = musicData.musicTitle
            currentAlbumTitle = musicData.albumTitle
            prepareAndPlay(getApplication(), musicData.audioFile)

            // 詳細画面へ遷移
            navController.navigate("detail/${musicData.musicTitle}/${musicData.albumTitle}")
        }
    }

    fun onValueChange(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun onNext(navController: NavController) {
        currentTrackIndex = (currentTrackIndex + 1) % musicList.size
        val nextMusic = musicList[currentTrackIndex]

        // 再生中の曲情報を更新
        currentMusicTitle = nextMusic.musicTitle
        currentAlbumTitle = nextMusic.albumTitle

        // 次の曲を再生準備
        prepareAndPlay(getApplication(), nextMusic.audioFile)

        // 次の曲の詳細画面へ遷移
        navController.navigate("detail/${nextMusic.musicTitle}/${nextMusic.albumTitle}") {
            popUpTo("detail/${nextMusic.musicTitle}/${nextMusic.albumTitle}") {
                inclusive = true
            }
        }
    }

    fun onPrevious(navController: NavController) {
        currentTrackIndex =
            if (currentTrackIndex - 1 < 0) trackList.size - 1 else currentTrackIndex - 1
        val previousMusic = musicList[currentTrackIndex]

        // 再生中の曲情報を更新
        currentMusicTitle = previousMusic.musicTitle
        currentAlbumTitle = previousMusic.albumTitle

        // 次の曲を再生準備
        prepareAndPlay(getApplication(), previousMusic.audioFile)

        // NavControllerを使って次の曲の画面に遷移
        navController.navigate("detail/${previousMusic.musicTitle}/${previousMusic.albumTitle}") {
            // 現在の画面スタックをクリアして戻らないようにする
            popUpTo("detail/${previousMusic.musicTitle}/${previousMusic.albumTitle}") {
                inclusive = true
            }
        }
    }

    fun onClose(navController: NavController) {
        navController.navigate("com/example/music_player_of_874wokiite/features/musicList") {
            popUpTo("com/example/music_player_of_874wokiite/features/musicList") {
                inclusive = true
            }
        }
    }

    override fun onCleared() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onCleared()
    }

    init {
        viewModelScope.launch {
            while (true) {
                mediaPlayer?.let {
                    if (_isPlaying.value == true) {
                        _currentPosition.postValue(it.currentPosition)
                    }
                }
                delay(50)
            }
        }
    }
}
