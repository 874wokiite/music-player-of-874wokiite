package com.example.music_player_of_874wokiite.top

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.example.music_player_of_874wokiite.model.musicList
import com.example.music_player_of_874wokiite.ui.MusicDetailScreen
import com.example.music_player_of_874wokiite.ui.theme.MusicPlayerOf874wokiiteTheme
import com.example.music_player_of_874wokiite.utils.MediaPlayerHandler
import com.example.music_player_of_874wokiite.utils.NfcHandler

class TopActivity : ComponentActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private var navController: NavController? = null
    private lateinit var nfcHandler: NfcHandler
    private val mediaPlayerHandler = MediaPlayerHandler()
    private var nfcAdapter: NfcAdapter? = null // Nullableに変更

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val musicViewModel: MusicViewModel by viewModels()
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // MediaPlayerの初期化
        mediaPlayer = mediaPlayerHandler.initializeMediaPlayer(this)

        setContent {
            val localNavController = rememberNavController()
            navController = localNavController

            // NfcHandlerのインスタンスを作成
            nfcHandler = NfcHandler(localNavController, this) // `this`でcontextを渡す

            MusicPlayerOf874wokiiteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    MusicApp(
                        musicViewModel = musicViewModel,
                        navController = localNavController
                    )
                }
            }
        }

        // NFCがサポートされていない場合のログ出力
        if (nfcAdapter == null) {
            Log.e("NFC_DEBUG", "NFC is not supported on this device")
        } else {
            // `setContent` が完了した後に NFC インテントを処理
            Handler(Looper.getMainLooper()).post { nfcHandler.handleNfcIntent(intent) }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (nfcAdapter != null) {
            nfcHandler.handleNfcIntent(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerHandler.releaseMediaPlayer() // MediaPlayerの解放
    }
}

@Composable
fun MusicApp(
    musicViewModel: MusicViewModel,
    navController: NavHostController
) {
    MusicPlayerOf874wokiiteTheme {
        NavHost(
            navController = navController,
            startDestination = "com/example/music_player_of_874wokiite/features/musicList",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
        ) {
            composable("com/example/music_player_of_874wokiite/features/musicList") {
                MusicListScreen(
                    navController = navController,
                    onRefreshPlay = { selectedMusicData ->
                        musicViewModel.onRefreshPlay(navController, selectedMusicData)
                    },
                    musicViewModel = musicViewModel
                )
            }
            composable(
                route = "detail/{musicTitle}/{albumTitle}",
                arguments =
                listOf(
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
                    musicList.find {
                        it.musicTitle == musicTitle && it.albumTitle == albumTitle
                    }

                // データが見つかった場合のみ詳細画面を表示
                musicData?.let {
                    MusicDetailScreen(
                        musicData = it,
                        musicViewModel = musicViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}