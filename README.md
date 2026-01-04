# ukyo_android_kuku

Android 向けの九九学習アプリです。`shared` モジュールは Kotlin Multiplatform で構成されており、
iOS 向けに `XCFramework` を生成して Xcode プロジェクトから利用できます。

## Xcode で開けない場合の原因と対応

このリポジトリには Android アプリ用の Gradle プロジェクトしか含まれていないため、ルートをそのまま
Xcode で開こうとしても「Android プロジェクトです」と見なされます。iOS から利用する場合は、
`shared` モジュールをフレームワークとして出力し、別途用意した iOS アプリの `.xcworkspace` に
組み込んでください。

最小構成で iOS から利用する手順の例:

1. XCFramework の生成

   ```bash
   ./gradlew :shared:assembleSharedDebugXCFramework
   ```

   `shared/build/XCFrameworks/debug/Shared.xcframework` が生成されます。リリース用は
   `assembleSharedReleaseXCFramework` を使います。

2. iOS プロジェクトへの組み込み

   - 生成した `Shared.xcframework` を任意の iOS アプリ プロジェクトに追加する
   - あるいは CocoaPods を使う場合は、`shared` モジュールの `podspec` を参照するように
     `Podfile` を作成し、`pod install` 後に生成された `.xcworkspace` を開く

これにより、Xcode でも `Shared` フレームワーク経由でロジックを再利用できます。

## 音声カスタマイズ

`app/src/main/res/raw` に `kuku_1_1.mp3` のような名前で録音した音声ファイルを配置すると、
学習画面やクイズの読み上げに実録音声が再生されます。ファイル名は `kuku_{左の数}_{右の数}` 形式で、
81 パターン分のファイルを置くことで全ての九九に対応できます。ファイルが存在しない場合は従来どおり
テキスト読み上げ (TTS) が使用されます。
