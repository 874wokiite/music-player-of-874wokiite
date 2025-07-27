package com.example.music_player_of_874wokiite.features.musiclist

import MusicContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.music_player_of_874wokiite.components.MiniPlayer
import com.example.music_player_of_874wokiite.components.MyScaffold
import com.example.music_player_of_874wokiite.model.MusicData
import com.example.music_player_of_874wokiite.model.musicList
import com.example.music_player_of_874wokiite.ui.theme.MusicPlayerOf874wokiiteTheme

// リスト表示画面
@Composable
fun MusicListScreen(
    navController: NavController,
    onRefreshPlay: (MusicData) -> Unit,
    musicViewModel: com.example.music_player_of_874wokiite.features.musicDetail.MusicViewModel
) {
    val currentMusicData by musicViewModel.currentMusicData.observeAsState()

    MyScaffold(
        title = "",
        onProfileClick = { navController.navigate("profile") }
    ) { innerPadding -> // Scaffoldの余白がここで渡される
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(
                    top = 24.dp, 
                    start = 8.dp, 
                    end = 8.dp, 
                    bottom = if (currentMusicData != null) 100.dp else 24.dp
                ),
            ) {
                items(musicList.size) { index ->
                    val musicData = musicList[index]
                    MusicContent(
                        coverImage = musicData.coverImage,
                        musicTitle = musicData.musicTitle,
                        albumTitle = musicData.albumTitle,
                        modifier = Modifier
                            .clickable {
                                // クリックで詳細画面へ遷移
                                onRefreshPlay(musicData)
                            }
                    )
                }
            }

            // ミニプレーヤーを下部に固定表示
            currentMusicData?.let { musicData ->
                MiniPlayer(
                    coverImageFileName = musicData.coverImage,
                    musicTitle = musicData.musicTitle,
                    albumTitle = musicData.albumTitle,
                    onClick = {
                        navController.navigate("detail/${musicData.musicTitle}/${musicData.albumTitle}")
                    },
                    musicViewModel = musicViewModel,
                    onPlayClick = { musicViewModel.onPlay() },
                    onPauseClick = { musicViewModel.onPause() },
                    onNextClick = { musicViewModel.onNextMiniPlayer() },
                    onPreviousClick = { musicViewModel.onPreviousMiniPlayer() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewMusicListScreen(modifier: Modifier = Modifier) {
    MusicPlayerOf874wokiiteTheme {
        // Preview用のコードは一時的にコメントアウト
        // 実際のViewModelが必要なため
    }
}