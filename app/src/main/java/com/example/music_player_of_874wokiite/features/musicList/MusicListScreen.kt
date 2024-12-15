package com.example.music_player_of_874wokiite.features.musiclist

import MusicContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.music_player_of_874wokiite.components.MyScaffold
import com.example.music_player_of_874wokiite.ui.theme.MusicPlayerOf874wokiiteTheme


// 音楽データを表すデータクラス
data class MusicData(
    val coverImage: String,
    val musicTitle: String,
    val albumTitle: String,
    val audioFile: String,
)

// リストデータ
val musicList = listOf(
    MusicData("happyReborn.png", "Haribo", "Haribo Album", "happyReborn.mp3"),
    MusicData("xxxDay.png", "xxxDay", "xxxDay", "happyReborn.mp3"),
    MusicData("rememberApathy.png", "思い出したアパシー", "思い出したアパシー", "rememberApathy.mp3"),
    MusicData("goGoGo.png", "はしろ", "xxxDay", "rememberApathy.mp3"),
    MusicData("doku.png", "毒", "xxxDay", "happyReborn.mp3")
)

// リスト表示画面
@Composable
fun MusicListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onRefreshPlay: (MusicData) -> Unit,
) {
    MyScaffold(
        title = "",
        onProfileClick = { navController.navigate("profile") }
    ) { innerPadding -> // Scaffoldの余白がここで渡される
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(musicList.size) { index ->
                val musicData = musicList[index]
                MusicContent(
                    coverImage = musicData.coverImage,
                    musicTitle = musicData.musicTitle,
                    albumTitle = musicData.albumTitle,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            // クリックで詳細画面へ遷移
                            onRefreshPlay(musicData)
                        }
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewMusicListScreen(modifier: Modifier = Modifier) {
    MusicPlayerOf874wokiiteTheme {
        MusicListScreen(
            navController = NavController(LocalContext.current),
            modifier = modifier,
            onRefreshPlay = {}
        )
    }
}