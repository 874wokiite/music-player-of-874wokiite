package com.example.music_player_of_874wokiite.utils

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

object DotEnvLoader {
    
    private const val TAG = "DotEnvLoader"
    private const val ENV_FILE_NAME = ".env"
    private var envVariables: Map<String, String> = emptyMap()
    private var isLoaded = false
    
    /**
     * .envファイルをassetsから読み込みます
     */
    fun load(context: Context) {
        if (isLoaded) return
        
        try {
            val envMap = mutableMapOf<String, String>()
            
            context.assets.open(ENV_FILE_NAME).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    reader.forEachLine { line ->
                        parseLine(line)?.let { (key, value) ->
                            envMap[key] = value
                        }
                    }
                }
            }
            
            envVariables = envMap
            isLoaded = true
            Log.d(TAG, "Successfully loaded ${envVariables.size} environment variables from .env file")
            
        } catch (e: Exception) {
            Log.w(TAG, ".env file not found or could not be read: ${e.message}")
            envVariables = emptyMap()
            isLoaded = true
        }
    }
    
    /**
     * .envファイルの1行をパースしてキー・値のペアを返します
     */
    private fun parseLine(line: String): Pair<String, String>? {
        val trimmedLine = line.trim()
        
        // 空行やコメント行をスキップ
        if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
            return null
        }
        
        val equalIndex = trimmedLine.indexOf('=')
        if (equalIndex == -1) {
            Log.w(TAG, "Invalid line format: $line")
            return null
        }
        
        val key = trimmedLine.substring(0, equalIndex).trim()
        val value = trimmedLine.substring(equalIndex + 1).trim()
            .removeSurrounding("\"")  // ダブルクォートを削除
            .removeSurrounding("'")   // シングルクォートを削除
        
        return key to value
    }
    
    /**
     * 環境変数の値を取得します（システム環境変数 → .env の順）
     */
    fun getEnv(key: String): String? {
        // システム環境変数を優先
        System.getenv(key)?.let { return it }
        
        // .envファイルから取得
        return envVariables[key]
    }
    
    /**
     * .envファイルが正常に読み込まれているかチェック
     */
    fun isEnvFileLoaded(): Boolean = isLoaded && envVariables.isNotEmpty()
    
    /**
     * 読み込まれた環境変数の一覧を取得（デバッグ用）
     */
    fun getLoadedVariables(): Set<String> = envVariables.keys
    
    /**
     * 環境変数の状態をログ出力
     */
    fun logStatus() {
        Log.d(TAG, "DotEnv Status:")
        Log.d(TAG, "  Loaded: $isLoaded")
        Log.d(TAG, "  Variables count: ${envVariables.size}")
        Log.d(TAG, "  Available keys: ${envVariables.keys.joinToString(", ")}")
    }
}