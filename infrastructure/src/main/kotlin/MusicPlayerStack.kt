import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.services.iam.*
import software.amazon.awscdk.services.s3.*
import software.constructs.Construct

class MusicPlayerStack(scope: Construct, id: String, props: StackProps) : Stack(scope, id, props) {
    
    init {
        // S3バケット作成
        val musicBucket = Bucket.Builder.create(this, "MusicBucket")
            .bucketName("music-player-bucket-${System.currentTimeMillis()}")
            .versioned(false)
            .publicReadAccess(false)
            .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
            .build()
        
        // IAMユーザー作成
        val appUser = User.Builder.create(this, "MusicAppUser")
            .userName("music-app-user")
            .build()
        
        // IAMポリシー作成
        val policy = Policy.Builder.create(this, "MusicAppPolicy")
            .policyName("music-app-s3-policy")
            .statements(listOf(
                PolicyStatement.Builder.create()
                    .effect(Effect.ALLOW)
                    .actions(listOf(
                        "s3:GetObject",
                        "s3:ListBucket"
                    ))
                    .resources(listOf(
                        musicBucket.bucketArn,
                        "${musicBucket.bucketArn}/*"
                    ))
                    .build()
            ))
            .users(listOf(appUser))
            .build()
        
        // アクセスキー作成
        val accessKey = AccessKey.Builder.create(this, "MusicAppAccessKey")
            .user(appUser)
            .build()
        
        // 出力として表示
        software.amazon.awscdk.CfnOutput.Builder.create(this, "BucketName")
            .value(musicBucket.bucketName)
            .description("S3 Bucket Name for Music Files")
            .build()
            
        software.amazon.awscdk.CfnOutput.Builder.create(this, "AccessKeyId")
            .value(accessKey.accessKeyId)
            .description("IAM Access Key ID")
            .build()
            
        software.amazon.awscdk.CfnOutput.Builder.create(this, "SecretAccessKey")
            .value(accessKey.secretAccessKey.unsafeUnwrap())
            .description("IAM Secret Access Key")
            .build()
    }
}