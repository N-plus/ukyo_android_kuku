package com.example.kukutrainer.ui.learning

import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kukutrainer.R
import com.example.kukutrainer.audio.BgmPlayer
import com.example.kukutrainer.audio.playRecordedKuku
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.navigation.Screen
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningScreen(stage: Int, navController: NavHostController) {
    val context = LocalContext.current
    var currentIndex by remember { mutableStateOf(1) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val tts = remember { TextToSpeech(context) { } }

    // result of current problem
    val result = stage * currentIndex

    // colorful gradient colors
    val gradientColors = listOf(
        Color(0xFFFF6B9D),
        Color(0xFF4ECDC4),
        Color(0xFFFFE66D),
        Color(0xFF95E1D3),
        Color(0xFFFF8A80)
    )

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF8BBD9),
            Color(0xFFE8F5E8),
            Color(0xFFFFE5B4)
        )
    )
    val infiniteTransition = rememberInfiniteTransition()
    val homeButtonScale by infiniteTransition.animateFloat(

        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    if (!PreferencesManager.isVoiceOn(context)) {
        return
    }

    fun playAudio() {
        mediaPlayer?.release()
        if (!playRecordedKuku(context, stage, currentIndex)) {
            val text = context.getString(
                R.string.learning_expression_format,
                stage,
                currentIndex,
                result
            )
            tts.language = Locale.JAPANESE
            tts.setSpeechRate(PreferencesManager.getSoundSpeed(context))
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun nextProblem() {
        if (currentIndex < 9) {
            currentIndex++
        } else {
            PreferencesManager.setStageCompleted(context, stage, true)
            val prev = PreferencesManager.getStarCount(context, stage)
            PreferencesManager.setStarCount(context, stage, prev + 1)
            navController.navigate(Screen.Completion.createRoute(stage))
        }
    }

    DisposableEffect(Unit) {
        BgmPlayer.stop()
        onDispose {
            tts.shutdown()
            mediaPlayer?.release()
            BgmPlayer.start(context)
        }
    }
    LaunchedEffect(Unit) {
        playAudio()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FloatingActionButton(
                        onClick = { navController.navigate(Screen.Home.route) },
                        modifier = Modifier
                            .size(64.dp)
                            .scale(homeButtonScale),
                        containerColor = Color(0xFF4ECDC4),
                        contentColor = Color.White
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = stringResource(id = R.string.home),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.home),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "${stage}の段",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF008EB9)
            )
            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = true,
                enter = scaleIn(animationSpec = tween(500)) + fadeIn(),
                exit = scaleOut(animationSpec = tween(500)) + fadeOut()
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stage.toString(),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = gradientColors[0],
                                modifier = Modifier
                                    .background(
                                        color = gradientColors[0].copy(alpha = 0.1f),
                                        shape = CircleShape
                                    )
                                    .padding(8.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "×",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3436)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = currentIndex.toString(),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = gradientColors[1],
                                modifier = Modifier
                                    .background(
                                        color = gradientColors[1].copy(alpha = 0.1f),
                                        shape = CircleShape
                                    )
                                    .padding(8.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "=",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3436)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = result.toString(),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = gradientColors[2],
                                modifier = Modifier
                                    .background(
                                        color = gradientColors[2].copy(alpha = 0.1f),
                                        shape = CircleShape
                                    )
                                    .padding(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = "おんせいをきいて",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = "こえにだしてよんでみよう",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3436),
                            textAlign = TextAlign.Center
                        )

                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FloatingActionButton(
                    onClick = { playAudio() },
                    modifier = Modifier.size(64.dp),
                    containerColor = Color(0xFF4ECDC4),
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = stringResource(id = R.string.play_sound),
                        modifier = Modifier.size(32.dp)
                    )
                }

                FloatingActionButton(
                    onClick = { nextProblem() },
                    modifier = Modifier.size(64.dp),
                    containerColor = Color(0xFFFFE66D),
                    contentColor = Color(0xFF2D3436)
                ) {
                    Icon(
                        imageVector = Icons.Default.NavigateNext,
                        contentDescription = stringResource(id = R.string.next),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
