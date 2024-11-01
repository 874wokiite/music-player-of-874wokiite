import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.example.music_player_of_874wokiite.R
import com.example.music_player_of_874wokiite.ui.loadBitmapFromAssets


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