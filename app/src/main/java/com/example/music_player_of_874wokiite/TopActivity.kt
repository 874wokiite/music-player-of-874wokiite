package com.example.music_player_of_874wokiite

import android.content.Intent
import android.media.MediaPlayer
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.music_player_of_874wokiite.features.musicDetail.MusicViewModel
import com.example.music_player_of_874wokiite.features.musiclist.MusicListScreen
import com.example.music_player_of_874wokiite.features.musiclist.musicList
import com.example.music_player_of_874wokiite.ui.MusicDetailScreen
import com.example.music_player_of_874wokiite.ui.theme.MusicPlayerOf874wokiiteTheme
import com.example.music_player_of_874wokiite.utils.MediaPlayerHandler
import com.example.music_player_of_874wokiite.utils.NfcHandler


class TopActivity : ComponentActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var nfcAdapter: NfcAdapter
    private var navController: NavController? = null // ComposeのNavControllerを保持
    private lateinit var nfcHandler: NfcHandler
    private val mediaPlayerHandler = MediaPlayerHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val musicViewModel: MusicViewModel by viewModels()
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            ?: throw IllegalStateException("NFCがサポートされていません")

        // MediaPlayerの初期化
        mediaPlayer = mediaPlayerHandler.initializeMediaPlayer(this)

        setContent {
            val localNavController = rememberNavController()
            navController = localNavController // ComposeのNavControllerを保持

            // NfcHandlerのインスタンスを作成
            nfcHandler = NfcHandler(navController)

            MusicPlayerOf874wokiiteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MusicApp(
                        musicViewModel = musicViewModel,
                        modifier = Modifier.padding(innerPadding),
                        navController = localNavController
                    )
                }
            }
        }

        // `setContent` が完了した後に NFC インテントを処理
        Handler(Looper.getMainLooper()).post {
            nfcHandler.handleNfcIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        nfcHandler.handleNfcIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerHandler.releaseMediaPlayer() // MediaPlayerの解放
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MusicApp(
    musicViewModel: MusicViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "com/example/music_player_of_874wokiite/features/musicList",
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
    ) {
        composable("com/example/music_player_of_874wokiite/features/musicList") {
            MusicListScreen(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                onRefreshPlay = { selectedMusicData ->
                    musicViewModel.onRefreshPlay(navController, selectedMusicData)
                }
            )
        }
        composable(
            route = "detail/{musicTitle}/{albumTitle}",
            arguments = listOf(
                navArgument("musicTitle") { type = NavType.StringType },
                navArgument("albumTitle") { type = NavType.StringType }
            ),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            val musicTitle = backStackEntry.arguments?.getString("musicTitle") ?: ""
            val albumTitle = backStackEntry.arguments?.getString("albumTitle") ?: ""

            // 楽曲データの検索
            val musicData =
                musicList.find { it.musicTitle == musicTitle && it.albumTitle == albumTitle }
            val isPlaying by musicViewModel.isPlaying.observeAsState(false)

            // データが見つかった場合のみ詳細画面を表示
            musicData?.let {
                MusicDetailScreen(
                    coverImage = it.coverImage,
                    musicTitle = it.musicTitle,
                    albumTitle = it.albumTitle,
                    modifier = Modifier.fillMaxSize(),
                    isPlaying = isPlaying,
                    onPlay = { if (!isPlaying) musicViewModel.onPlay() },
                    onPause = { if (isPlaying) musicViewModel.onPause() },
                    onClose = { musicViewModel.onClose(navController) },
                    onNext = { musicViewModel.onNext(navController) },
                    onPrevious = { musicViewModel.onPrevious(navController) },
                    currentPosition = musicViewModel.currentPosition.observeAsState(0).value,
                    duration = musicViewModel.duration.observeAsState(0).value,
                    onValueChange = { musicViewModel.onValueChange(it.toInt()) }
                )
            }
        }
    }
}
