import software.amazon.awscdk.App
import software.amazon.awscdk.Environment
import software.amazon.awscdk.StackProps

fun main() {
    val app = App()
    
    MusicPlayerStack(
        app, 
        "MusicPlayerStack",
        StackProps.builder()
            .env(Environment.builder()
                .region("ap-northeast-1") // 東京リージョン
                .build())
            .build()
    )
    
    app.synth()
}