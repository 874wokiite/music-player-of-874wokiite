package com.example.music_player_of_874wokiite.components

import androidx.compose.ui.tooling.preview.Preview
import com.example.music_player_of_874wokiite.R
import com.example.music_player_of_874wokiite.ui.theme.MusicPlayerOf874wokiiteTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffold(
    title: String = "",
    onProfileClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_topbar),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(64.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            title,
                            color = MaterialTheme.colorScheme.inverseSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Image(
                            painter = painterResource(id = R.drawable.profile_874),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        content = content
    )
}

@Preview
@Composable
fun PreviewMyScaffold(modifier: Modifier = Modifier) {
    MusicPlayerOf874wokiiteTheme {
        MyScaffold(
            title = "",
            onProfileClick = {},
            content = {}
        )
    }
}