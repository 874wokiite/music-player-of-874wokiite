package com.example.music_player_of_874wokiite

import android.media.MediaPlayer
import android.os.Bundle
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.music_player_of_874wokiite.features.musicDetail.MusicViewModel
import com.example.music_player_of_874wokiite.ui.MusicDetailScreen
import com.example.music_player_of_874wokiite.features.musiclist.MusicListScreen
import com.example.music_player_of_874wokiite.features.musiclist.musicList
import com.example.music_player_of_874wokiite.ui.theme.MusicPlayerOf874wokiiteTheme


class TopActivity : ComponentActivity() {
    private lateinit var mediaPlayer: MediaPlayer // MediaPlayerをクラスメンバーとして定義

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // `MusicViewModel`のインスタンスを取得
        val musicViewModel: MusicViewModel by viewModels()

        setContent {
            MusicPlayerOf874wokiiteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // `MusicApp`に`musicViewModel`と`innerPadding`を渡す
                    MusicApp(
                        musicViewModel = musicViewModel,
                        modifier = Modifier.padding(innerPadding) // パディングを追加
                    )
                }
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Activityが破棄されるときにMediaPlayerもリリース
    }
    }


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MusicApp(musicViewModel: MusicViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    // 画面遷移を管理
    NavHost(
        navController = navController,
        startDestination = "com/example/music_palyer_of_874wokiite/features/musicList",
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
    ) {
        composable("com/example/music_palyer_of_874wokiite/features/musicList") {
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

            // `musicList`から、タイトルとアルバムに一致するアイテムを探す
            val musicData = musicList.find { it.musicTitle == musicTitle && it.albumTitle == albumTitle }
            val isPlaying by musicViewModel.isPlaying.observeAsState(false)

            // 見つかった場合、`MusicDetailScreen`を表示
            musicData?.let {
                MusicDetailScreen(
                    coverImage = it.coverImage,
                    musicTitle = it.musicTitle,
                    albumTitle = it.albumTitle,
                    modifier = Modifier.fillMaxSize(),
                    // 再生停止関連
                    isPlaying = isPlaying,
                    onPlay = {
                        if (!isPlaying) {
                            musicViewModel.onPlay()
                        }
                    },
                    onPause = {
                        if (isPlaying) {
                            musicViewModel.onPause()
                        }
                    },
                    onClose = {
                        musicViewModel.onClose(navController)  // TopActivityに戻る
                    },
                    onNext = {
                        musicViewModel.onNext(navController) // NavControllerを渡す
                    },
                    onPrevious = {
                        musicViewModel.onPrevious(navController)
                    },
                    // Seekbarの変数
                    currentPosition = musicViewModel.currentPosition.observeAsState(0).value,
                    duration = musicViewModel.duration.observeAsState(0).value,
                    onValueChange = {
                        musicViewModel.onValueChange(it.toInt())
                    },
                )
            }
        }
    }
}