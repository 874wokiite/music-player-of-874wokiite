package com.example.music_player_of_874wokiite.repository

import android.content.Context
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.example.music_player_of_874wokiite.model.MusicData
import com.example.music_player_of_874wokiite.utils.EnvironmentCredentialsManager
import java.net.URL
import java.util.*

class S3Repository(private val context: Context) {
    
    private var s3Client: AmazonS3Client? = null
    private var bucketName: String? = null
    private val credentialsManager = EnvironmentCredentialsManager(context)
    
    init {
        initializeFromEnvironment()
    }
    
    fun initialize(accessKeyId: String, secretAccessKey: String, bucketName: String) {
        val credentials = BasicAWSCredentials(accessKeyId, secretAccessKey)
        this.s3Client = AmazonS3Client(credentials).apply {
            setRegion(Region.getRegion(Regions.AP_NORTHEAST_1))
        }
        this.bucketName = bucketName
    }
    
    private fun initializeFromEnvironment() {
        if (credentialsManager.hasCredentials()) {
            val accessKeyId = credentialsManager.getAccessKeyId()
            val secretAccessKey = credentialsManager.getSecretAccessKey()
            val bucketName = credentialsManager.getBucketName()
            
            if (accessKeyId != null && secretAccessKey != null && bucketName != null) {
                initialize(accessKeyId, secretAccessKey, bucketName)
            }
        }
    }
    
    fun isInitialized(): Boolean {
        return s3Client != null && bucketName != null
    }
    
    private fun generatePresignedUrl(s3Key: String, expiration: Date): String? {
        return try {
            val client = s3Client ?: return null
            val bucket = bucketName ?: return null
            
            val request = GeneratePresignedUrlRequest(bucket, s3Key)
            request.expiration = expiration
            
            val url = client.generatePresignedUrl(request)
            url.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    fun createS3MusicData(
        s3Key: String,
        musicTitle: String,
        albumTitle: String,
        coverImageKey: String? = null
    ): MusicData? {
        val client = s3Client ?: return null
        val bucket = bucketName ?: return null
        
        // 24時間有効なPre-signed URLを生成
        val expiration = Calendar.getInstance().apply {
            add(Calendar.HOUR, 24)
        }.time
        
        val audioUrl = generatePresignedUrl(s3Key, expiration) ?: return null
        val coverUrl = coverImageKey?.let { generatePresignedUrl(it, expiration) } ?: ""
        
        return MusicData(
            coverImage = coverUrl,
            musicTitle = musicTitle,
            albumTitle = albumTitle,
            audioFile = "", // ローカルファイルパスは空
            isRemote = true,
            s3Url = audioUrl,
            s3BucketName = bucket,
            s3Key = s3Key
        )
    }
    
    fun listMusicFiles(): List<String> {
        return try {
            val client = s3Client ?: return emptyList()
            val bucket = bucketName ?: return emptyList()
            
            val objectListing = client.listObjects(bucket, "audio/")
            objectListing.objectSummaries
                .filter { it.key.endsWith(".mp3") || it.key.endsWith(".m4a") }
                .map { it.key }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}