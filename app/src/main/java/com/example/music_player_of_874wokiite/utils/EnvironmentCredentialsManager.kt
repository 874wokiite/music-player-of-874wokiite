package com.example.music_player_of_874wokiite.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class EnvironmentCredentialsManager(private val context: Context) {
    
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "aws_credentials"
        private const val KEY_ACCESS_KEY_ID = "access_key_id"
        private const val KEY_SECRET_ACCESS_KEY = "secret_access_key"
        private const val KEY_BUCKET_NAME = "bucket_name"
        private const val TAG = "EnvironmentCredentialsManager"
        
        // 環境変数名
        private const val ENV_ACCESS_KEY_ID = "AWS_ACCESS_KEY_ID"
        private const val ENV_SECRET_ACCESS_KEY = "AWS_SECRET_ACCESS_KEY"
        private const val ENV_BUCKET_NAME = "AWS_S3_BUCKET_NAME"
    }
    
    init {
        // .envファイルを読み込み
        DotEnvLoader.load(context)
    }
    
    /**
     * 環境変数から認証情報を取得（システム環境変数 → .env ファイル）
     */
    private fun getFromEnvironment(): Triple<String?, String?, String?> {
        val accessKeyId = DotEnvLoader.getEnv(ENV_ACCESS_KEY_ID)
        val secretAccessKey = DotEnvLoader.getEnv(ENV_SECRET_ACCESS_KEY)
        val bucketName = DotEnvLoader.getEnv(ENV_BUCKET_NAME)
        
        if (accessKeyId != null && secretAccessKey != null && bucketName != null) {
            val source = if (System.getenv(ENV_ACCESS_KEY_ID) != null) "system environment" else ".env file"
            Log.d(TAG, "AWS credentials loaded from $source")
        }
        
        return Triple(accessKeyId, secretAccessKey, bucketName)
    }
    
    /**
     * 認証情報をSharedPreferencesに保存
     */
    fun saveCredentials(accessKeyId: String, secretAccessKey: String, bucketName: String) {
        sharedPrefs.edit()
            .putString(KEY_ACCESS_KEY_ID, accessKeyId)
            .putString(KEY_SECRET_ACCESS_KEY, secretAccessKey)
            .putString(KEY_BUCKET_NAME, bucketName)
            .apply()
        
        Log.d(TAG, "AWS credentials saved to SharedPreferences")
    }
    
    /**
     * アクセスキーIDを取得（環境変数 → SharedPreferences の順）
     */
    fun getAccessKeyId(): String? {
        val (envAccessKeyId, _, _) = getFromEnvironment()
        return envAccessKeyId ?: sharedPrefs.getString(KEY_ACCESS_KEY_ID, null)
    }
    
    /**
     * シークレットアクセスキーを取得（環境変数 → SharedPreferences の順）
     */
    fun getSecretAccessKey(): String? {
        val (_, envSecretAccessKey, _) = getFromEnvironment()
        return envSecretAccessKey ?: sharedPrefs.getString(KEY_SECRET_ACCESS_KEY, null)
    }
    
    /**
     * S3バケット名を取得（環境変数 → SharedPreferences の順）
     */
    fun getBucketName(): String? {
        val (_, _, envBucketName) = getFromEnvironment()
        return envBucketName ?: sharedPrefs.getString(KEY_BUCKET_NAME, null)
    }
    
    /**
     * 認証情報が利用可能かチェック
     */
    fun hasCredentials(): Boolean {
        val accessKeyId = getAccessKeyId()
        val secretAccessKey = getSecretAccessKey()
        val bucketName = getBucketName()
        
        return !accessKeyId.isNullOrBlank() && 
               !secretAccessKey.isNullOrBlank() && 
               !bucketName.isNullOrBlank()
    }
    
    /**
     * SharedPreferencesの認証情報をクリア（環境変数は影響しない）
     */
    fun clearCredentials() {
        sharedPrefs.edit()
            .remove(KEY_ACCESS_KEY_ID)
            .remove(KEY_SECRET_ACCESS_KEY)
            .remove(KEY_BUCKET_NAME)
            .apply()
        
        Log.d(TAG, "AWS credentials cleared from SharedPreferences")
    }
    
    /**
     * 認証情報の取得元を確認
     */
    fun getCredentialsSource(): String {
        val (envAccessKeyId, envSecretAccessKey, envBucketName) = getFromEnvironment()
        val hasEnvCredentials = envAccessKeyId != null && envSecretAccessKey != null && envBucketName != null
        val hasPrefsCredentials = sharedPrefs.contains(KEY_ACCESS_KEY_ID) && 
                                  sharedPrefs.contains(KEY_SECRET_ACCESS_KEY) && 
                                  sharedPrefs.contains(KEY_BUCKET_NAME)
        
        return when {
            hasEnvCredentials -> {
                // システム環境変数か.envファイルかを判定
                if (System.getenv(ENV_ACCESS_KEY_ID) != null) {
                    "System Environment Variables"
                } else {
                    ".env File"
                }
            }
            hasPrefsCredentials -> "SharedPreferences"
            else -> "None"
        }
    }
    
    /**
     * デバッグ用：認証情報の状態を表示
     */
    fun logCredentialsStatus() {
        val source = getCredentialsSource()
        val accessKeyId = getAccessKeyId()
        val bucketName = getBucketName()
        
        Log.d(TAG, "Credentials Status:")
        Log.d(TAG, "  Source: $source")
        Log.d(TAG, "  Has Credentials: ${hasCredentials()}")
        Log.d(TAG, "  Access Key ID: ${accessKeyId?.let { "${it.take(8)}..." } ?: "null"}")
        Log.d(TAG, "  Bucket Name: $bucketName")
    }
}