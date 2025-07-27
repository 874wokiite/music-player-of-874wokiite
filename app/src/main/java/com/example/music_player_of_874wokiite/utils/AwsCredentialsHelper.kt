package com.example.music_player_of_874wokiite.utils

import android.content.Context
import android.util.Log

object AwsCredentialsHelper {
    
    private const val TAG = "AwsCredentialsHelper"
    
    /**
     * AWS認証情報をSharedPreferencesに保存します
     */
    fun saveCredentials(
        context: Context,
        accessKeyId: String,
        secretAccessKey: String,
        bucketName: String
    ): Boolean {
        return try {
            val credentialsManager = EnvironmentCredentialsManager(context)
            credentialsManager.saveCredentials(accessKeyId, secretAccessKey, bucketName)
            Log.i(TAG, "AWS credentials saved successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save AWS credentials", e)
            false
        }
    }
    
    /**
     * 認証情報が存在するかチェックします（環境変数 → SharedPreferences）
     */
    fun hasCredentials(context: Context): Boolean {
        return try {
            val credentialsManager = EnvironmentCredentialsManager(context)
            credentialsManager.hasCredentials()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to check credentials", e)
            false
        }
    }
    
    /**
     * SharedPreferencesの認証情報を削除します（環境変数は影響しない）
     */
    fun clearCredentials(context: Context): Boolean {
        return try {
            val credentialsManager = EnvironmentCredentialsManager(context)
            credentialsManager.clearCredentials()
            Log.i(TAG, "AWS credentials cleared")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to clear credentials", e)
            false
        }
    }
    
    /**
     * アクセスキーIDを取得します（環境変数 → SharedPreferences）
     */
    fun getAccessKeyId(context: Context): String? {
        return try {
            val credentialsManager = EnvironmentCredentialsManager(context)
            credentialsManager.getAccessKeyId()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get access key ID", e)
            null
        }
    }
    
    /**
     * シークレットアクセスキーを取得します（環境変数 → SharedPreferences）
     */
    fun getSecretAccessKey(context: Context): String? {
        return try {
            val credentialsManager = EnvironmentCredentialsManager(context)
            credentialsManager.getSecretAccessKey()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get secret access key", e)
            null
        }
    }
    
    /**
     * S3バケット名を取得します（環境変数 → SharedPreferences）
     */
    fun getBucketName(context: Context): String? {
        return try {
            val credentialsManager = EnvironmentCredentialsManager(context)
            credentialsManager.getBucketName()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get bucket name", e)
            null
        }
    }
    
    /**
     * 認証情報の取得元を確認
     */
    fun getCredentialsSource(context: Context): String {
        return try {
            val credentialsManager = EnvironmentCredentialsManager(context)
            credentialsManager.getCredentialsSource()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get credentials source", e)
            "Error"
        }
    }
    
    /**
     * デバッグ用：認証情報の状態を表示
     */
    fun logCredentialsStatus(context: Context) {
        try {
            val credentialsManager = EnvironmentCredentialsManager(context)
            credentialsManager.logCredentialsStatus()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to log credentials status", e)
        }
    }
}