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

@SuppressLint("RememberReturnType")
@Composable
fun MusicContent(coverImage:String, musicTitle: String, albumTitle:String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bitmap = remember { loadBitmapFromAssets(context, coverImage) }
    
    Column {
        bitmap.let {
            if (it != null) {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = modifier
                )
            }
        }
        Text(
            text = musicTitle,
            color = colorResource(id = R.color.textPrimary),
            modifier = modifier,
        )
        Text(
            text = albumTitle,
            color = colorResource(id = R.color.textSecondary),
            modifier = modifier,
        )

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MusicApp() {
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = "musicList",
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
    ) {
        composable("musicList") {
            MusicListGrid(navController)
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
            // MusicContentを直接呼び出し
            MusicContent(
                coverImage = "rememberApathy.png", // 必要なら画像パスを渡す
                musicTitle = musicTitle,
                albumTitle = albumTitle,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}




@Composable
fun MusicListGrid(navController: NavController, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
    ) {
        items(6) { index ->
            MusicContent(
                coverImage = "rememberApathy.png",
                musicTitle = "思い出したアパシー",
                albumTitle = "思い出したアパシー",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        // クリックで詳細画面へ遷移
                        navController.navigate("detail/思い出したアパシー/思い出したアパシー")
                    }
            )
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