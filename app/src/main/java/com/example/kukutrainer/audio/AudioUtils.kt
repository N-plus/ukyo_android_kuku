package com.example.kukutrainer.audio

import android.content.Context
import android.media.MediaPlayer

/**
 * Plays a recorded multiplication sound from the raw resources.
 * Files should be named in the pattern `kuku_{{left}}_{{right}}`.
 * Returns true if the audio was played, or false if the resource doesn't exist.
 */
fun playRecordedKuku(context: Context, left: Int, right: Int): Boolean {
    val resName = "kuku_${left}_${right}"
    val resId = context.resources.getIdentifier(resName, "raw", context.packageName)
    if (resId == 0) {
        return false
    }
    val player = MediaPlayer.create(context, resId) ?: return false
    player.setOnCompletionListener { mp ->
        mp.release()
    }
    return try {
        player.start()
        true
    } catch (e: Exception) {
        player.release()
        false
    }
}