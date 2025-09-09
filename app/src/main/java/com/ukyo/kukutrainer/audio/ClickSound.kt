package com.ukyo.kukutrainer.audio

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import com.ukyo.kukutrainer.R

/** Plays the button click sound. */
fun playButtonClickSound(context: Context) {
    val player = MediaPlayer.create(context, R.raw.bottonclick)
    player?.setOnCompletionListener { it.release() }
    try {
        player?.start()
    } catch (_: Exception) {
        player?.release()
    }
}

/** Returns an onClick lambda that also plays a click sound. */
@Composable
fun withClickSound(onClick: () -> Unit): () -> Unit {
    val context = LocalContext.current
    return {
        playButtonClickSound(context)
        onClick()
    }
}

/** Modifier extension that plays a click sound before invoking [onClick]. */
fun Modifier.clickableWithSound(enabled: Boolean = true, onClick: () -> Unit): Modifier = composed {
    val context = LocalContext.current
    clickable(enabled = enabled) {
        playButtonClickSound(context)
        onClick()
    }
}
