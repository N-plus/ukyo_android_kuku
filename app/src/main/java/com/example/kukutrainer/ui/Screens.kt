package com.example.kukutrainer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import com.example.kukutrainer.navigation.Screen
import com.example.kukutrainer.R
import com.example.kukutrainer.data.PreferencesManager
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.ui.unit.dp
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.background
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Switch
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun SplashScreen(onFinished: (Boolean) -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        delay(3000)
        val selected = isCharacterSelected(context)
        onFinished(selected)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val context = LocalContext.current
            val drawable = AppCompatResources.getDrawable(context, R.mipmap.ic_launcher)
            if (drawable != null) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.splash_tagline))
        }
    }
}

private fun isCharacterSelected(context: Context): Boolean {
    return PreferencesManager.getSelectedCharacter(context) != 0
}


@Composable
fun CharacterSelectionScreen(navController: NavHostController) {
    val context = LocalContext.current
    var selected by remember { mutableStateOf(PreferencesManager.getSelectedCharacter(context)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.select_character_prompt), style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        CharacterCard(
            label = stringResource(id = R.string.character_1),
            selected = selected == 1,
            onClick = {
                selected = 1
                PreferencesManager.setSelectedCharacter(context, 1)
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.CharacterSelection.route) { inclusive = true }
                }
            }
        )
        CharacterCard(
            label = stringResource(id = R.string.character_2),
            selected = selected == 2,
            onClick = {
                selected = 2
                PreferencesManager.setSelectedCharacter(context, 2)
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.CharacterSelection.route) { inclusive = true }
                }
            }
        )
        CharacterCard(
            label = stringResource(id = R.string.character_3),
            selected = selected == 3,
            onClick = {
                selected = 3
                PreferencesManager.setSelectedCharacter(context, 3)
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.CharacterSelection.route) { inclusive = true }
                }
            }
        )
    }
}

@Composable
private fun CharacterCard(label: String, selected: Boolean, onClick: () -> Unit) {
    val border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        border = border,
        colors = CardDefaults.cardColors()
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun StageCard(stage: Int, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.stage_format, stage))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = subtitle)
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val character = remember { PreferencesManager.getSelectedCharacter(context) }

    var targetScale by remember { mutableStateOf(0.8f) }
    val scale by animateFloatAsState(targetValue = targetScale, label = "charScale")

    LaunchedEffect(Unit) { targetScale = 1f }

    val totalStars = remember { (1..9).sumOf { PreferencesManager.getStarCount(context, it) } }
    val completedStages = remember { (1..9).count { PreferencesManager.isStageCompleted(context, it) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate(Screen.LearningStageSelect.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = stringResource(id = R.string.learning_mode))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Screen.QuizDifficultySelect.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = stringResource(id = R.string.quiz_mode))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = stringResource(id = R.string.total_stars, totalStars))
        Text(text = stringResource(id = R.string.completed_stages, completedStages))

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.go_to_profile),
            modifier = Modifier.clickable { navController.navigate(Screen.Profile.route) }
        )
    }
}


@Composable
fun LearningStageSelectScreen(navController: NavHostController) {
    val context = LocalContext.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items((1..9).toList()) { stage ->
            val completed = PreferencesManager.isStageCompleted(context, stage)
            val stars = PreferencesManager.getStarCount(context, stage)
            val status = when {
                completed -> stringResource(id = R.string.learning_status_completed)
                stars > 0 -> stringResource(id = R.string.learning_status_in_progress)
                else -> stringResource(id = R.string.learning_status_not_started)
            }
            val subtitle = if (completed) "${stars}★" else status
            StageCard(stage, subtitle) {
                navController.navigate(Screen.Learning.createRoute(stage))
            }
        }
    }
}


@Composable
fun LearningScreen(stage: Int, navController: NavHostController) {
    val context = LocalContext.current
    var index by remember { mutableStateOf(1) }
    val tts = remember { TextToSpeech(context) { } }
    DisposableEffect(Unit) { onDispose { tts.shutdown() } }
    LaunchedEffect(Unit) { tts.language = Locale.JAPANESE }
    LaunchedEffect(index) {
        val text = context.getString(
            R.string.learning_expression_format,
            stage,
            index,
            stage * index
        )
        tts.setSpeechRate(PreferencesManager.getSoundSpeed(context))
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val result = stage * index
            Text(text = stringResource(id = R.string.learning_expression_format, stage, index, result))
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
        }
    }
}

@Composable
fun CompletionScreen(stage: Int, navController: NavHostController) {
    val context = LocalContext.current
    val stars = remember { PreferencesManager.getStarCount(context, stage) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FeedbackAnimation(state = FeedbackState.Completed)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.congratulations))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${stars}★")
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { navController.navigate(Screen.Home.route) }) {
                Text(text = stringResource(id = R.string.back_to_home))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate(Screen.Learning.createRoute(stage + 1)) }) {
                Text(text = stringResource(id = R.string.next_stage))
            }
        }
    }
}

@Composable
fun QuizDifficultySelectScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { navController.navigate(Screen.Quiz.createRoute(1)) }) {
                Text(text = stringResource(id = R.string.easy))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate(Screen.Quiz.createRoute(2)) }) {
                Text(text = stringResource(id = R.string.normal))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate(Screen.Quiz.createRoute(3)) }) {
                Text(text = stringResource(id = R.string.hard))
            }
        }
    }
}

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

    val coroutineScope = rememberCoroutineScope()
    val tts = remember { TextToSpeech(context) { } }
    DisposableEffect(Unit) { onDispose { tts.shutdown() } }
    LaunchedEffect(Unit) { tts.language = Locale.JAPANESE }

    fun speak(text: String) {
        tts.setSpeechRate(PreferencesManager.getSoundSpeed(context))
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    LaunchedEffect(left, right) {
        speak(context.getString(R.string.quiz_question_format, left, right))
    }

    // ここで stringResource を取得（Composable コンテキスト内）
    val correctText = stringResource(id = R.string.quiz_correct)
    val wrongHintText = stringResource(id = R.string.quiz_wrong_hint)
    val backText = stringResource(id = R.string.back_to_home)

    fun nextQuestion() {
        left = range.random()
        right = range.random()
        options = generateOptions(left, right, range)
        feedback = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // フォーマット付きの stringResource はその都度呼び出して構わない（stars / left / right は状態）
        Text(text = stringResource(id = R.string.quiz_stars, stars))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.quiz_question_format, left, right))
        Spacer(modifier = Modifier.height(16.dp))

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


@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    var bgmOn by remember { mutableStateOf(PreferencesManager.isBgmOn(context)) }
    var fast by remember { mutableStateOf(PreferencesManager.getSoundSpeed(context) >= 1f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.bgm), modifier = Modifier.weight(1f))
            Switch(checked = bgmOn, onCheckedChange = {
                bgmOn = it
                PreferencesManager.setBgmOn(context, it)
            })
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            val label = if (fast) stringResource(id = R.string.sound_speed_fast) else stringResource(id = R.string.sound_speed_slow)
            Text(text = label, modifier = Modifier.weight(1f))
            Switch(checked = fast, onCheckedChange = {
                fast = it
                PreferencesManager.setSoundSpeed(context, if (it) 1f else 0.5f)
            })
        }

        Button(onClick = { navController.navigate(Screen.CharacterSelection.route) }) {
            Text(text = stringResource(id = R.string.change_character))
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.language_placeholder), modifier = Modifier.weight(1f))
            Switch(checked = false, onCheckedChange = {}, enabled = false)
        }
    }
}

@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val totalStars = remember { (1..9).sumOf { PreferencesManager.getStarCount(context, it) } }
    val selectedCharacter = remember { PreferencesManager.getSelectedCharacter(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(id = R.string.profile_title), style = MaterialTheme.typography.headlineLarge)

        Text(text = stringResource(id = R.string.profile_total_stars, totalStars))

        Text(text = stringResource(id = R.string.profile_badges))
        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.height(120.dp)) {
            items((1..9).toList()) { stage ->
                val completed = PreferencesManager.isStageCompleted(context, stage)
                val color = if (completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(40.dp)
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stage.toString())
                }
            }
        }

        val charLabel = when (selectedCharacter) {
            1 -> stringResource(id = R.string.character_1)
            2 -> stringResource(id = R.string.character_2)
            3 -> stringResource(id = R.string.character_3)
            else -> stringResource(id = R.string.character_1)
        }
        Text(text = stringResource(id = R.string.profile_selected_character, charLabel))

        Spacer(modifier = Modifier.weight(1f))

        Text(text = stringResource(id = R.string.dressup_placeholder))
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