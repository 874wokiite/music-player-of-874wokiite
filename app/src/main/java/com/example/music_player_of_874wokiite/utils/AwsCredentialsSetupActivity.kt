package com.example.music_player_of_874wokiite.utils

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.music_player_of_874wokiite.features.musicDetail.MusicViewModel
import com.example.music_player_of_874wokiite.ui.theme.MusicPlayerOf874wokiiteTheme

/**
 * AWS認証情報を設定するためのサンプルActivity
 * 実際のアプリでは、この機能を設定画面などに組み込んでください
 */
class AwsCredentialsSetupActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MusicPlayerOf874wokiiteTheme {
                AwsCredentialsSetupScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AwsCredentialsSetupScreen(
    musicViewModel: MusicViewModel = viewModel()
) {
    val context = LocalContext.current
    
    var accessKeyId by remember { mutableStateOf("") }
    var secretAccessKey by remember { mutableStateOf("") }
    var bucketName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    
    // 初期化時に既存の認証情報があるかチェック
    LaunchedEffect(Unit) {
        val hasCredentials = musicViewModel.checkCredentialsStatus()
        if (hasCredentials) {
            message = "認証情報が既に設定されています"
            accessKeyId = AwsCredentialsHelper.getAccessKeyId(context) ?: ""
            bucketName = AwsCredentialsHelper.getBucketName(context) ?: ""
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "AWS S3 認証情報設定",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        OutlinedTextField(
            value = accessKeyId,
            onValueChange = { accessKeyId = it },
            label = { Text("Access Key ID") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            enabled = !isLoading
        )
        
        OutlinedTextField(
            value = secretAccessKey,
            onValueChange = { secretAccessKey = it },
            label = { Text("Secret Access Key") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            enabled = !isLoading
        )
        
        OutlinedTextField(
            value = bucketName,
            onValueChange = { bucketName = it },
            label = { Text("S3 Bucket Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            enabled = !isLoading
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    isLoading = true
                    val success = musicViewModel.setAwsCredentials(
                        accessKeyId.trim(),
                        secretAccessKey.trim(),
                        bucketName.trim()
                    )
                    message = if (success) {
                        "認証情報が正常に保存されました"
                    } else {
                        "認証情報の保存に失敗しました"
                    }
                    isLoading = false
                },
                enabled = !isLoading && accessKeyId.isNotBlank() && 
                         secretAccessKey.isNotBlank() && bucketName.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("保存")
                }
            }
            
            OutlinedButton(
                onClick = {
                    val success = musicViewModel.clearAwsCredentials()
                    message = if (success) {
                        "認証情報をクリアしました"
                    } else {
                        "認証情報のクリアに失敗しました"
                    }
                    accessKeyId = ""
                    secretAccessKey = ""
                    bucketName = ""
                },
                enabled = !isLoading
            ) {
                Text("クリア")
            }
        }
        
        if (message.isNotEmpty()) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        
        // 認証情報ソース表示
        val credentialsSource = remember { AwsCredentialsHelper.getCredentialsSource(context) }
        if (credentialsSource != "None") {
            Text(
                text = "認証情報ソース: $credentialsSource",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        
        // .env設定の説明
        TextButton(
            onClick = {
                message = """
                    .env ファイルでの設定方法:
                    
                    1. app/src/main/assets/.env.example を .env にコピー
                    2. 実際の値を設定:
                       AWS_ACCESS_KEY_ID=your_access_key
                       AWS_SECRET_ACCESS_KEY=your_secret_key
                       AWS_S3_BUCKET_NAME=your_bucket_name
                    3. アプリを再起動
                """.trimIndent()
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(".env ファイル設定方法")
        }
        
        // .envファイルの状態表示
        TextButton(
            onClick = {
                DotEnvLoader.logStatus()
                val isLoaded = DotEnvLoader.isEnvFileLoaded()
                val variables = DotEnvLoader.getLoadedVariables()
                message = if (isLoaded) {
                    ".env ファイルが読み込まれています\n変数数: ${variables.size}\nキー: ${variables.joinToString(", ")}"
                } else {
                    ".env ファイルが見つからないか読み込めません"
                }
            },
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(".env ファイル状態確認")
        }
    }
}