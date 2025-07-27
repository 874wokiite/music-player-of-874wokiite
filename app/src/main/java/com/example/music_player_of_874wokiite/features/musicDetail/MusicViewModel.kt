package com.example.music_player_of_874wokiite.features.musicDetail

// 再生に必要なロジックなど
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.music_player_of_874wokiite.model.MusicData
import com.example.music_player_of_874wokiite.model.musicList
import com.example.music_player_of_874wokiite.repository.S3Repository
import com.example.music_player_of_874wokiite.utils.EnvironmentCredentialsManager
import com.example.music_player_of_874wokiite.utils.AwsCredentialsHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {
    private var exoPlayer: ExoPlayer? = null
    private val s3Repository = S3Repository(application)
    private val credentialsManager = EnvironmentCredentialsManager(application)

    // MutableLiveDataで再生状態を管理
    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _currentPosition = MutableLiveData(0)
    val currentPosition: LiveData<Int> = _currentPosition

    private val _duration = MutableLiveData(0)
    val duration: LiveData<Int> = _duration

    // 現在再生中の楽曲情報
    private val _currentMusicData = MutableLiveData<MusicData?>()
    val currentMusicData: LiveData<MusicData?> = _currentMusicData

    private val trackList = musicList.map { it.audioFile } // 仮のトラックリスト
    private var currentTrackIndex = 0

    private var currentMusicTitle: String? = null
    private var currentAlbumTitle: String? = null

    init {
        initializeExoPlayer()
        initializeS3Repository()
    }
    
    private fun initializeExoPlayer() {
        exoPlayer = ExoPlayer.Builder(getApplication()).build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_READY -> {
                            _duration.value = duration.toInt()
                            if (playWhenReady) {
                                _isPlaying.value = true
                                Log.d("MusicViewModel", "Playback started for: $currentMusicTitle")
                            }
                        }
                        Player.STATE_ENDED -> {
                            _isPlaying.value = false
                        }
                    }
                }
                
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = isPlaying
                }
            })
        }
    }
    
    private fun initializeS3Repository() {
        if (credentialsManager.hasCredentials()) {
            val accessKeyId = credentialsManager.getAccessKeyId()!!
            val secretAccessKey = credentialsManager.getSecretAccessKey()!!
            val bucketName = credentialsManager.getBucketName()!!
            s3Repository.initialize(accessKeyId, secretAccessKey, bucketName)
        }
    }

    private fun prepareAndPlay(context: Context, musicData: MusicData) {
        Log.d("MusicViewModel", "Preparing to play: ${musicData.musicTitle}")
        viewModelScope.launch {
            try {
                val mediaItem = if (musicData.isRemote) {
                    // S3 URLから再生
                    MediaItem.fromUri(musicData.getAudioUrl())
                } else {
                    // ローカルアセットから再生
                    val assetUri = "asset:///${musicData.audioFile}"
                    MediaItem.fromUri(assetUri)
                }
                
                exoPlayer?.apply {
                    setMediaItem(mediaItem)
                    prepare()
                    playWhenReady = true
                }
            } catch (e: Exception) {
                Log.e("MusicViewModel", "Error preparing audio file: ${musicData.audioFile}", e)
            }
        }
    }

    fun onPause() {
        exoPlayer?.pause()
        _isPlaying.value = false
    }

    fun onPlay() {
        exoPlayer?.play()
        _isPlaying.value = true
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
            _currentMusicData.value = musicData
            // currentTrackIndexも更新
            currentTrackIndex = musicList.indexOf(musicData)
            prepareAndPlay(getApplication(), musicData)

            // 詳細画面へ遷移
            navController.navigate("detail/${musicData.musicTitle}/${musicData.albumTitle}")
        }
    }

    fun onValueChange(position: Int) {
        exoPlayer?.seekTo(position.toLong())
    }

    fun onNext(navController: NavController) {
        currentTrackIndex = (currentTrackIndex + 1) % musicList.size
        val nextMusic = musicList[currentTrackIndex]

        // 再生中の曲情報を更新
        currentMusicTitle = nextMusic.musicTitle
        currentAlbumTitle = nextMusic.albumTitle
        _currentMusicData.value = nextMusic

        // 次の曲を再生準備
        prepareAndPlay(getApplication(), nextMusic)

        // 次の曲の詳細画面へ遷移
        navController.navigate("detail/${nextMusic.musicTitle}/${nextMusic.albumTitle}") {
            popUpTo("detail/${nextMusic.musicTitle}/${nextMusic.albumTitle}") {
                inclusive = true
            }
        }
    }

    // ミニプレーヤー用：画面遷移しない次の楽曲
    fun onNextMiniPlayer() {
        currentTrackIndex = (currentTrackIndex + 1) % musicList.size
        val nextMusic = musicList[currentTrackIndex]

        // 再生中の曲情報を更新
        currentMusicTitle = nextMusic.musicTitle
        currentAlbumTitle = nextMusic.albumTitle
        _currentMusicData.value = nextMusic

        // 次の曲を再生準備
        prepareAndPlay(getApplication(), nextMusic)
    }

    fun onPrevious(navController: NavController) {
        currentTrackIndex =
            if (currentTrackIndex - 1 < 0) trackList.size - 1 else currentTrackIndex - 1
        val previousMusic = musicList[currentTrackIndex]

        // 再生中の曲情報を更新
        currentMusicTitle = previousMusic.musicTitle
        currentAlbumTitle = previousMusic.albumTitle
        _currentMusicData.value = previousMusic

        // 次の曲を再生準備
        prepareAndPlay(getApplication(), previousMusic)

        // NavControllerを使って次の曲の画面に遷移
        navController.navigate("detail/${previousMusic.musicTitle}/${previousMusic.albumTitle}") {
            // 現在の画面スタックをクリアして戻らないようにする
            popUpTo("detail/${previousMusic.musicTitle}/${previousMusic.albumTitle}") {
                inclusive = true
            }
        }
    }

    // ミニプレーヤー用：画面遷移しない前の楽曲
    fun onPreviousMiniPlayer() {
        currentTrackIndex =
            if (currentTrackIndex - 1 < 0) trackList.size - 1 else currentTrackIndex - 1
        val previousMusic = musicList[currentTrackIndex]

        // 再生中の曲情報を更新
        currentMusicTitle = previousMusic.musicTitle
        currentAlbumTitle = previousMusic.albumTitle
        _currentMusicData.value = previousMusic

        // 前の曲を再生準備
        prepareAndPlay(getApplication(), previousMusic)
    }

    fun onClose(navController: NavController) {
        navController.navigate("com/example/music_player_of_874wokiite/features/musicList") {
            popUpTo("com/example/music_player_of_874wokiite/features/musicList") {
                inclusive = true
            }
        }
    }

    override fun onCleared() {
        exoPlayer?.release()
        exoPlayer = null
        super.onCleared()
    }

    init {
        viewModelScope.launch {
            while (true) {
                exoPlayer?.let {
                    if (_isPlaying.value == true) {
                        _currentPosition.postValue(it.currentPosition.toInt())
                    }
                }
                delay(50)
            }
        }
    }
    
    // AWS認証情報を設定
    fun setAwsCredentials(accessKeyId: String, secretAccessKey: String, bucketName: String): Boolean {
        val success = AwsCredentialsHelper.saveCredentials(getApplication(), accessKeyId, secretAccessKey, bucketName)
        if (success) {
            s3Repository.initialize(accessKeyId, secretAccessKey, bucketName)
            Log.d("MusicViewModel", "AWS credentials configured successfully")
        } else {
            Log.e("MusicViewModel", "Failed to save AWS credentials")
        }
        return success
    }
    
    // 認証情報の状態をチェック
    fun checkCredentialsStatus(): Boolean {
        val hasCredentials = AwsCredentialsHelper.hasCredentials(getApplication())
        if (hasCredentials) {
            AwsCredentialsHelper.logCredentialsStatus(getApplication())
        }
        return hasCredentials && s3Repository.isInitialized()
    }
    
    // 認証情報をクリア
    fun clearAwsCredentials(): Boolean {
        return AwsCredentialsHelper.clearCredentials(getApplication())
    }
    
    // S3から音楽ファイル一覧を取得
    fun loadS3MusicFiles(): List<String> {
        return s3Repository.listMusicFiles()
    }
    
    // S3の音楽データを作成
    fun createS3MusicData(s3Key: String, musicTitle: String, albumTitle: String, coverImageKey: String? = null): MusicData? {
        return s3Repository.createS3MusicData(s3Key, musicTitle, albumTitle, coverImageKey)
    }
}
