package com.example.music_palyer_of_874wokiite

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.music_palyer_of_874wokiite.ui.theme.Musicpalyerof874wokiiteTheme

class TopActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Musicpalyerof874wokiiteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MusicListGrid()
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicListGrid(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),  // 2列で表示
        modifier = modifier
    ) {
        items(13) { index ->
            MusicContent(
                coverImage = "rememberApathy.png",
                musicTitle = "Music Title $index",
                albumTitle = "Album Title",
                modifier = Modifier.padding(8.dp)
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
        MusicListGrid()
    }
}