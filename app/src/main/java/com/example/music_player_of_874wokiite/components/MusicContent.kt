import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.music_player_of_874wokiite.features.musicDetail.loadBitmapFromAssets
import com.example.music_player_of_874wokiite.ui.theme.MusicPlayerOf874wokiiteTheme


@SuppressLint("RememberReturnType")
@Composable
fun MusicContent(coverImage:String, musicTitle: String, albumTitle:String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bitmap = remember { loadBitmapFromAssets(context, coverImage) }

    Column(
        modifier = modifier.padding(start = 8.dp, end = 8.dp, bottom = 40.dp)
    ) {
        bitmap.let {
            if (it != null) {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = modifier
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)) // 枠線もつける場合
                )
            }
        }
        Text(
            text = musicTitle,
            color = MaterialTheme.colorScheme.inverseSurface,
            modifier = modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = albumTitle,
            color = MaterialTheme.colorScheme.inverseSurface,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
fun PreviewMusicContent(modifier: Modifier = Modifier) {
    MusicPlayerOf874wokiiteTheme {
        MusicContent(
            coverImage = "xxxDay.png",
            musicTitle = "xxxDay",
            albumTitle = "xxxDay",
        )
    }
}