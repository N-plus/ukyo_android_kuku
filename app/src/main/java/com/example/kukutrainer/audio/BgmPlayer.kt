// app/src/main/java/com/example/kukutrainer/audio/BgmPlayer.kt
package com.example.kukutrainer.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import com.example.kukutrainer.R
import com.example.kukutrainer.data.PreferencesManager

/**
 * シンプルな BGM 再生ユーティリティ（シングルトン）
 *
 * MainActivity から:
 *   BgmPlayer.start(this)   // 再生 or 再開
 *   BgmPlayer.stop()        // 一時停止
 *   BgmPlayer.release()     // アプリ終了時に解放
 *
 * AudioFocus まで厳密に制御したい場合は AudioManager を追加で扱ってください。
 */
object BgmPlayer {

    // 再利用する MediaPlayer（null のときだけ生成）
    private var mediaPlayer: MediaPlayer? = null

    /** BGM を再生／再開する */
    fun start(context: Context) {
        if (!PreferencesManager.isBgmOn(context)) {
            stop()
            return
        }

        // 既に生成済みならそのまま再開
        mediaPlayer?.let {
            if (!it.isPlaying) it.start()
            return
        }

        // 初回生成：AudioAttributes を付与し、ループ再生
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)       // ゲーム/アプリ向け
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )

            // res/raw/bgm.mp3 を読み込む
            val uri = Uri.parse("android.resource://${context.packageName}/${R.raw.bgm}")
            setDataSource(context, uri)
            isLooping = true              // 無限ループ
            setOnCompletionListener { mp ->
                // 念のため完了時に先頭へ戻して再生し続ける
                mp.seekTo(0)
                mp.start()
            }
            prepare()                     // 同期準備（大きいファイルなら prepareAsync も可）
            start()
        }
    }

    /** BGM を一時停止する（ポーズなので resume 可能） */
    fun stop() {
        mediaPlayer?.takeIf { it.isPlaying }?.pause()
    }

    /** MediaPlayer を完全に解放する（onDestroy で呼び出し推奨） */
    fun release() {
        mediaPlayer?.run {
            reset()
            release()
        }
        mediaPlayer = null
    }
}
