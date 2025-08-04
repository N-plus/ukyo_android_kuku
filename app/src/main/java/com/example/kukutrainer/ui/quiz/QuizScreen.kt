package com.example.kukutrainer.ui.quiz

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import com.example.kukutrainer.R
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.navigation.Screen
import com.example.kukutrainer.ui.FeedbackAnimation
import com.example.kukutrainer.ui.FeedbackState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import androidx.compose.ui.unit.dp

@Composable
fun QuizScreen(difficulty: Int, navController: NavHostController) {
    val context = LocalContext.current
    val range = when (difficulty) {
        1 -> 1..3
        2 -> 1..6
        else -> 1..9
    }

    var left by rememberSaveable { mutableStateOf(range.random()) }
    var right by rememberSaveable { mutableStateOf(range.random()) }
    var options by remember { mutableStateOf(generateOptions(left, right, range)) }
    var feedbackState by remember { mutableStateOf(FeedbackState.None) }
    var hint by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }
    var stars by rememberSaveable { mutableStateOf(0) }
    var currentQuestion by rememberSaveable { mutableStateOf(1) }
    val totalQuestions = 10
    var userAnswer by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val tts = remember { TextToSpeech(context) { } }
    DisposableEffect(Unit) { onDispose { tts.shutdown() } }
    LaunchedEffect(Unit) { tts.language = Locale.JAPANESE }

    fun speak(text: String) {
        tts.language = Locale.JAPANESE
        tts.setSpeechRate(PreferencesManager.getSoundSpeed(context))
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun speakQuestion() {
        speak(context.getString(R.string.quiz_question_format, left, right))
    }

    val correctText = stringResource(id = R.string.quiz_correct)
    val wrongHintText = stringResource(id = R.string.quiz_wrong_hint)
    val backText = stringResource(id = R.string.back_to_home)

    fun nextQuestion() {
        currentQuestion++
        left = range.random()
        right = range.random()
        options = generateOptions(left, right, range)
        feedback = ""
        userAnswer = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.quiz_progress, currentQuestion, totalQuestions))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.quiz_question_format, left, right))
        Spacer(modifier = Modifier.height(16.dp))

        if (difficulty >= 3) {
            OutlinedTextField(
                value = userAnswer,
                onValueChange = { userAnswer = it.filter { ch -> ch.isDigit() } },
                label = { Text(stringResource(id = R.string.quiz_answer_hint)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (userAnswer.toIntOrNull() == left * right) {
                        stars++
                        feedbackState = FeedbackState.Correct
                        speak(correctText)
                        coroutineScope.launch {
                            delay(800)
                            nextQuestion()
                        }
                    } else {
                        hint = wrongHintText
                        feedbackState = FeedbackState.Incorrect
                        speak(wrongHintText)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            ) {
                Text(text = stringResource(id = R.string.quiz_submit))
            }
        } else {
            options.forEach { option ->
                Button(
                    onClick = {
                        if (option == left * right) {
                            stars++
                            feedbackState = FeedbackState.Correct
                            speak(correctText)
                            coroutineScope.launch {
                                delay(800)
                                nextQuestion()
                            }
                        } else {
                            hint = wrongHintText
                            feedbackState = FeedbackState.Incorrect
                            speak(wrongHintText)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = option.toString())
                }
            }
        }
        if (feedbackState != FeedbackState.None) {
            Spacer(modifier = Modifier.height(16.dp))
            FeedbackAnimation(state = feedbackState, hint = hint)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.navigate(Screen.Home.route) }) {
            Text(text = backText)
        }
    }
}

private fun generateOptions(left: Int, right: Int, range: IntRange): List<Int> {
    val correct = left * right
    val options = mutableSetOf(correct)
    while (options.size < 4) {
        val value = range.random() * range.random()
        options.add(value)
    }
    return options.shuffled()
}
