package com.example.kukutrainer.ui.learning

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.kukutrainer.R
import com.example.kukutrainer.audio.playRecordedKuku
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.navigation.Screen
import java.util.Locale
import androidx.compose.ui.unit.dp

@Composable
fun LearningScreen(stage: Int, navController: NavHostController) {
    val context = LocalContext.current
    var index by remember { mutableStateOf(1) }
    val tts = remember { TextToSpeech(context) { } }
    DisposableEffect(Unit) { onDispose { tts.shutdown() } }
    fun speakCurrent() {
        if (!playRecordedKuku(context, stage, index)) {
            val text = context.getString(
                R.string.learning_expression_format,
                stage,
                index,
                stage * index
            )
            tts.language = Locale.JAPANESE
            tts.setSpeechRate(PreferencesManager.getSoundSpeed(context))
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val result = stage * index
            Text(text = stringResource(id = R.string.learning_expression_format, stage, index, result))
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { speakCurrent() }) {
                Text(text = stringResource(id = R.string.play_sound))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                if (index < 9) {
                    index++
                } else {
                    PreferencesManager.setStageCompleted(context, stage, true)
                    val prev = PreferencesManager.getStarCount(context, stage)
                    PreferencesManager.setStarCount(context, stage, prev + 1)
                    navController.navigate(Screen.Completion.createRoute(stage))
                }
            }) {
                Text(text = stringResource(id = R.string.next))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { navController.navigate(Screen.Home.route) }) {
                Text(text = stringResource(id = R.string.back_to_home))
            }
        }
    }
}