package com.example.music_player_of_874wokiite.model

// 音楽データを表すデータクラス
data class MusicData(
    val coverImage: String,
    val musicTitle: String,
    val albumTitle: String,
    val audioFile: String,
    val isRemote: Boolean = false, // S3の音楽ファイルかどうか
    val s3Url: String? = null, // S3のURL（isRemoteがtrueの場合）
    val s3BucketName: String? = null, // S3バケット名
    val s3Key: String? = null // S3キー
) {
    // 実際の音楽ファイルのURLを取得
    fun getAudioUrl(): String {
        return if (isRemote && s3Url != null) {
            s3Url
        } else {
            audioFile
        }
    }
    
    // カバー画像のURLを取得
    fun getCoverImageUrl(): String {
        return if (isRemote && coverImage.startsWith("http")) {
            coverImage
        } else {
            coverImage
        }
    }
}