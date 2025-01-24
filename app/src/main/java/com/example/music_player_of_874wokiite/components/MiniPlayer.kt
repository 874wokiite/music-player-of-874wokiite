package com.example.music_player_of_874wokiite.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.music_player_of_874wokiite.features.musicDetail.MusicViewModel
import com.example.music_player_of_874wokiite.ui.theme.MusicPlayerOf874wokiiteTheme

// コメント
@Composable
fun MiniPlayer(
    coverImage: ImageBitmap?,
    musicTitle: String,
    albumTitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    musicViewModel: MusicViewModel,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit
) {
    val isPlaying by musicViewModel.isPlaying.observeAsState(false)
    val currentPosition by musicViewModel.currentPosition.observeAsState(0)
    val duration by musicViewModel.duration.observeAsState(0)

    Card(
        modifier =
        modifier
            .clickable { onClick() } // タップで詳細画面へ遷移
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // カバー画像
                coverImage?.let {
                    Image(
                        bitmap = it,
                        contentDescription = "$musicTitle cover image",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // 曲のタイトルとアルバム名
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = musicTitle,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = albumTitle,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // 再生・一時停止ボタン
                if (isPlaying) {
                    Button(onClick = onPauseClick) { Text("Pause") }
                } else {
                    Button(onClick = onPlayClick) { Text("Play") }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onNextClick) { Text("Next") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onPreviousClick) { Text("Previous") }
            }

            // シークバー（再生位置表示）
            SeekBar(
                currentPosition = currentPosition,
                duration = duration,
                onValueChange = { /* シークバーの値変更処理 */ },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Previewを表示
@Preview(showBackground = true)
@Composable
fun MiniPlayerPreview() {
    MusicPlayerOf874wokiiteTheme {
        MiniPlayer(
            musicViewModel = MusicViewModel(
                application = TODO()
            ),
            onPlayClick = {},
            onPauseClick = {},
            onNextClick = {},
            onPreviousClick = {},
            musicTitle = "xxxDay",
            albumTitle = "xxxDay",
            coverImage = null,
            onClick = {}
        )
    }
}
