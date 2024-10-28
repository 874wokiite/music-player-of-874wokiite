package com.example.music_palyer_of_874wokiite

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.music_palyer_of_874wokiite.ui.theme.Musicpalyerof874wokiiteTheme
import com.example.music_palyer_of_874wokiite.ui.MusicDetailScreen
import com.example.music_player_of_874wokiite.features.musiclist.MusicListScreen
import com.example.music_player_of_874wokiite.features.musiclist.musicList


class TopActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            Musicpalyerof874wokiiteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MusicApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MusicApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "com/example/music_palyer_of_874wokiite/features/musicList",
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
    ) {
        composable("com/example/music_palyer_of_874wokiite/features/musicList") {
            MusicListScreen(navController)
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

            // musicListから、タイトルとアルバムに一致するアイテムを探す
            val musicData = musicList.find { it.musicTitle == musicTitle && it.albumTitle == albumTitle }

            // 見つかった場合、MusicDetailScreenを表示
            musicData?.let {
                MusicDetailScreen(
                    coverImage = it.coverImage,
                    musicTitle = it.musicTitle,
                    albumTitle = it.albumTitle,
                    audioFile = it.audioFile,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


fun loadBitmapFromAssets(context: Context, fileName: String) =
    context.assets.open(fileName).use { BitmapFactory.decodeStream(it)?.asImageBitmap() }

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Musicpalyerof874wokiiteTheme {
        MusicApp()
    }
}