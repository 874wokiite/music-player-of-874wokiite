package com.example.music_player_of_874wokiite.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import com.example.music_player_of_874wokiite.features.musicDetail.SampleViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

data class SampleData(
    val name: String = "",
    val age: String = "",
    val birthday: String = ""
)

@Composable
fun SampleListScreen(viewModel: SampleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.fetchSampleList()
    }

    val sampleList by viewModel.sampleList.observeAsState(emptyList())

    val errorMessage by viewModel.errorMessage.observeAsState()

    println("konnnichiha! $sampleList")

    Column {
        Text(text = "サンプルリスト")
        errorMessage?.let {
            Text(text = "エラー: $it", color = Color.Red)
        }
        LazyColumn {
            items(sampleList) { sampleData ->
                Text(text = "名前: ${sampleData.name}, 年齢: ${sampleData.age}, 誕生日: ${sampleData.birthday}")
            }
        }
    }
}