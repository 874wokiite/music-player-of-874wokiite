# Music Player of 874wokiite

Android向けの音楽プレイヤーアプリです。NFCタグとAWS S3を使用した楽曲管理機能を備えています。

<img width="1262" alt="Screenshot 2025-01-22 at 1 15 00" src="https://github.com/user-attachments/assets/0f4d1840-46be-453f-a7ff-afff55dbc453" />

## 主な機能

- 音楽再生・一時停止・スキップ機能
- NFCタグによる楽曲選択
- AWS S3からの楽曲ストリーミング
- Jetpack Composeによるモダンなユーザーインターフェース
- Material 3デザインシステム
- ExoPlayerによる高品質な音声再生

## 技術スタック

- **言語**: Kotlin
- **UIフレームワーク**: Jetpack Compose
- **デザインシステム**: Material 3
- **音声再生**: ExoPlayer (Media3)
- **クラウドストレージ**: AWS S3
- **通信**: NFC
- **アーキテクチャ**: MVVM
- **最小SDKバージョン**: 24 (Android 7.0)
- **対象SDKバージョン**: 34 (Android 14)

## プロジェクト構成

```
app/src/main/java/com/example/music_player_of_874wokiite/
├── components/           # 再利用可能なUIコンポーネント
├── features/            # 機能別モジュール
│   ├── musicDetail/     # 音楽詳細画面
│   └── musicList/       # 音楽リスト画面
├── model/              # データモデル
├── repository/         # データアクセス層
├── utils/              # ユーティリティクラス
├── viewmodel/          # ViewModelクラス
└── ui/theme/           # テーマ設定
```

## セットアップ

### 必要な環境
- Android Studio Hedgehog | 2023.1.1以降
- JDK 11
- Android SDK API 34

### ビルド手順

1. プロジェクトをクローンします
```bash
git clone <repository-url>
cd music-player-of-874wokiite
```

2. Android Studioでプロジェクトを開きます

3. AWS認証情報を設定します（S3機能を使用する場合）

4. プロジェクトをビルドして実行します

## 依存関係

主要な依存関係は以下の通りです：

- Jetpack Compose BOM
- AndroidX Core KTX
- Material 3
- AWS Android SDK (Core & S3)
- ExoPlayer (Media3)
- Accompanist Navigation Animation

## 権限

アプリは以下の権限を使用します：

- `NFC` - NFCタグの読み取り
- `INTERNET` - ネットワーク通信
- `READ_EXTERNAL_STORAGE` - 外部ストレージからのファイル読み取り
- `MODIFY_AUDIO_SETTINGS` - 音声設定の変更
- `BLUETOOTH` & `BLUETOOTH_ADMIN` - Bluetooth音声出力

## インフラストラクチャ

`infrastructure/`ディレクトリにはAWS CDKを使用したクラウドインフラストラクチャの定義が含まれています。
