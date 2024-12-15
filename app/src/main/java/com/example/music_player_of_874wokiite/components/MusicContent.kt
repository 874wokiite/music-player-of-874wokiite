import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.music_player_of_874wokiite.R
import com.example.music_player_of_874wokiite.features.musiclist.MusicListScreen
import com.example.music_player_of_874wokiite.ui.loadBitmapFromAssets
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
                    modifier = modifier.padding(bottom = 8.dp)
                )
            }
        }
        Text(
            text = musicTitle,
            color = MaterialTheme.colorScheme.inverseSurface,
            modifier = modifier,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = albumTitle,
            color = MaterialTheme.colorScheme.inverseSurface,
            modifier = modifier,
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