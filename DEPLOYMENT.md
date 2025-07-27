# AWS S3対応音楽プレーヤー デプロイメントガイド

このプロジェクトは、Kotlin CDKを使用してAWS S3インフラを構築し、Android音楽プレーヤーアプリからS3の音楽ファイルをストリーミング再生できます。

## プロジェクト構成

```
music-player-project/
├── app/                 # Androidアプリケーション
├── infrastructure/      # Kotlin CDK インフラストラクチャコード
└── DEPLOYMENT.md       # このファイル
```

## 前提条件

1. AWS CLI がインストール・設定済み
2. AWS CDK v2 がインストール済み (`npm install -g aws-cdk`)
3. Android Studio
4. Gradle

## デプロイ手順

### 1. AWS インフラ構築

```bash
cd infrastructure
./gradlew build
cdk deploy
```

デプロイ完了後、以下の情報が出力されます：
- `BucketName`: S3バケット名
- `AccessKeyId`: IAMアクセスキー
- `SecretAccessKey`: IAMシークレットアクセスキー

### 2. S3バケットに音楽ファイルをアップロード

```bash
# 音楽ファイルをaudio/フォルダにアップロード
aws s3 cp your-music-file.mp3 s3://[BucketName]/audio/

# カバー画像をcovers/フォルダにアップロード
aws s3 cp your-cover-image.jpg s3://[BucketName]/covers/
```

### 3. Androidアプリでの設定

**オプション1: .envファイルを使用（推奨）**
```bash
# 1. .env.exampleファイルをコピー
cp app/src/main/assets/.env.example app/src/main/assets/.env

# 2. .envファイルを編集
# app/src/main/assets/.env
AWS_ACCESS_KEY_ID=your_access_key_here
AWS_SECRET_ACCESS_KEY=your_secret_key_here
AWS_S3_BUCKET_NAME=music-player-bucket-1753587727671
```

**オプション2: 設定画面を使用**
```kotlin
// 設定画面を起動
val intent = Intent(this, AwsCredentialsSetupActivity::class.java)
startActivity(intent)
```

**オプション3: ViewModelで直接設定**
```kotlin
// ViewModelで認証情報を設定（SharedPreferencesに保存）
val success = musicViewModel.setAwsCredentials(
    accessKeyId = "your_access_key_id",
    secretAccessKey = "your_secret_access_key", 
    bucketName = "your_bucket_name"
)

// 認証情報の状態をチェック
val isConfigured = musicViewModel.checkCredentialsStatus()
```

**認証情報の優先順位**
1. システム環境変数（`AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_S3_BUCKET_NAME`）
2. .envファイル（`app/src/main/assets/.env`）
3. SharedPreferences（アプリで設定した値）

**セキュリティ機能**
- .envファイルでの安全な認証情報管理
- .gitignoreで.envファイルを除外（機密情報保護）
- ハードコードされた認証情報の排除
- 自動初期化機能
- 認証情報取得元の確認機能

**注意事項**
- `.env`ファイルは.gitignoreに含まれているため、各環境で個別に設定が必要
- `.env.example`ファイルをテンプレートとして使用

## 使用技術

### インフラ（Kotlin CDK）
- **S3**: 音楽ファイル・カバー画像ストレージ
- **IAM**: アクセス権限管理（最小権限）
- **Pre-signed URLs**: セキュアな一時アクセス

### Android アプリ
- **AWS SDK for Android**: S3との連携
- **ExoPlayer**: 高品質音楽再生
- **EncryptedSharedPreferences**: 認証情報の安全な保存
- **Jetpack Compose**: UI

## セキュリティ

- IAMユーザーは最小権限（S3 GetObject, ListBucket のみ）
- Pre-signed URLs で一時的なアクセス（24時間有効）
- Android Keystore で認証情報暗号化保存
- S3バケットはプライベート設定

## インフラ削除

```bash
cd infrastructure
cdk destroy
```

## 注意事項

- S3の料金は使用量に応じて発生します
- Pre-signed URLsは24時間で期限切れになります
- IAM認証情報は安全に管理してください