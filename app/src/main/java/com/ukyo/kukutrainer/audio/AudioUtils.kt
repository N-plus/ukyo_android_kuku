package com.ukyo.kukutrainer.audio

import android.content.Context
import android.media.MediaPlayer
import com.ukyo.kukutrainer.R

/**
 * Plays a recorded multiplication sound from the raw resources.
 * Files should be named in the pattern `kuku_{{left}}_{{right}}`.
 * Returns the [MediaPlayer] instance if the audio was played, or null if the resource doesn't exist.
 */
fun playRecordedKuku(context: Context, left: Int, right: Int): MediaPlayer? {
    val resName = "kuku_${left}_${right}"
    val resId = context.resources.getIdentifier(resName, "raw", context.packageName)
    if (resId == 0) {
        return null
    }
    val player = MediaPlayer.create(context, resId) ?: return null
    player.setOnCompletionListener { mp ->
        mp.release()
    }
    return try {
        player.start()
        player
    } catch (e: Exception) {
        player.release()
        null
    }
}

/**
 * Plays a simple feedback sound to indicate whether the answer was
 * correct or incorrect. The sound resource is released automatically
 * after playback finishes.
 */
fun playFeedbackSound(context: Context, isCorrect: Boolean) {
    val resId = if (isCorrect) R.raw.pinpon else R.raw.bubu
    val player = MediaPlayer.create(context, resId)
    player?.setOnCompletionListener { mp ->
        mp.release()
    }
    try {
        player?.start()
    } catch (e: Exception) {
        player?.release()
    }
}
