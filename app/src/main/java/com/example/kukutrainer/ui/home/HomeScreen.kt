@file:OptIn(ExperimentalAnimationApi::class)

package com.example.kukutrainer.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kukutrainer.navigation.Screen
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun HomeScreen(navController: NavHostController) {
    KidsHomeScreen(
        onStudyModeClick = { navController.navigate(Screen.LearningStageSelect.route) },
        onQuizModeClick = { navController.navigate(Screen.QuizDifficultySelect.route) },
        onSettingsClick = { navController.navigate(Screen.Settings.route) },
        onProfileClick = { navController.navigate(Screen.Profile.route) }
    )
}

@Composable
fun KidsHomeScreen(
    onStudyModeClick: () -> Unit = {},
    onQuizModeClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "background")

    val backgroundOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "background_animation"
    )

    val cloudOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cloud_animation"
    )

    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF87CEEB),
                        Color(0xFFB0E0E6),
                        Color(0xFFE0F6FF)
                    )
                )
            )
    ) {
        FloatingClouds(cloudOffset)
        FloatingSparkles(backgroundOffset)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            AnimatedVisibility(
                visible = showContent,
                enter = slideInFromTop() + fadeIn()
            ) {
                KidsHeader()
            }

            Spacer(modifier = Modifier.height(40.dp))

            AnimatedVisibility(
                visible = showContent,
                enter = slideInFromBottom() + fadeIn()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    KidsMenuButton(
                        text = "くくをまなぶ",
                        emoji = "📚",
                        colors = listOf(Color(0xFFFF6B9D), Color(0xFFFF8E9B)),
                        onClick = onStudyModeClick
                    )

                    KidsMenuButton(
                        text = "クイズ",
                        emoji = "🎯",
                        colors = listOf(Color(0xFF4ECDC4), Color(0xFF44A08D)),
                        onClick = onQuizModeClick
                    )

                    KidsMenuButton(
                        text = "せってい",
                        emoji = "⚙️",
                        colors = listOf(Color(0xFFFFBE0B), Color(0xFFF39F17)),
                        onClick = onSettingsClick
                    )

                    KidsMenuButton(
                        text = "プロフィール",
                        emoji = "👤",
                        colors = listOf(Color(0xFFD4EF5E), Color(0xFFEFB747)),
                        onClick = onProfileClick
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(1000))
            ) {
                Text(
                    text = "たのしく がんばろう！ 🌟",
                    fontSize = 16.sp,
                    color = Color(0xFF2C3E50),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
        }
    }
}

@Composable
fun KidsHeader() {
    val bounceScale by rememberInfiniteTransition(label = "header").animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .scale(bounceScale)
                .shadow(8.dp, RoundedCornerShape(50.dp))
                .clip(RoundedCornerShape(50.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFEB3B),
                            Color(0xFFFF9800)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🦄", fontSize = 48.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "おかえりなさい！",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50)
        )

        Text(
            text = "きょうも いっしょに あそぼうね",
            fontSize = 16.sp,
            color = Color(0xFF34495E),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun KidsMenuButton(
    text: String,
    emoji: String,
    colors: List<Color>,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_scale"
    )

    val wiggle by rememberInfiniteTransition(label = "wiggle").animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wiggle_animation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            }
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(25.dp)
            ),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.horizontalGradient(colors = colors)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = emoji,
                    fontSize = 32.sp,
                    modifier = Modifier.offset(x = wiggle.dp)
                )

                Text(
                    text = text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

@Composable
fun FloatingClouds(offset: Float) {
    val clouds = remember {
        List(4) {
            Cloud(
                x = Random.nextFloat(),
                y = Random.nextFloat() * 0.6f,
                size = Random.nextFloat() * 40 + 30,
                speed = Random.nextFloat() * 0.5f + 0.2f
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        clouds.forEach { cloud ->
            val animatedX = (cloud.x + (offset * cloud.speed)) % 1.2f - 0.2f
            drawCircle(
                color = Color.White.copy(alpha = 0.7f),
                radius = cloud.size,
                center = Offset(
                    x = animatedX * size.width,
                    y = cloud.y * size.height
                )
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.6f),
                radius = cloud.size * 0.8f,
                center = Offset(
                    x = animatedX * size.width + cloud.size * 0.5f,
                    y = cloud.y * size.height - cloud.size * 0.3f
                )
            )
        }
    }
}

@Composable
fun FloatingSparkles(offset: Float) {
    val sparkles = remember {
        List(20) {
            Sparkle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 8 + 4,
                speed = Random.nextFloat() * 3 + 1,
                color = listOf(
                    Color(0xFFFFD700),
                    Color(0xFFFF69B4),
                    Color(0xFF00CED1),
                    Color(0xFF98FB98),
                    Color(0xFFDDA0DD),
                    Color(0xFFFFB347)
                ).random()
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        sparkles.forEach { sparkle ->
            val animatedY = (sparkle.y + (sin(offset * 0.02f * sparkle.speed) * 0.1f)) % 1f
            val animatedX = (sparkle.x + (offset * sparkle.speed * 0.0005f)) % 1f

            drawCircle(
                color = sparkle.color.copy(alpha = 0.8f),
                radius = sparkle.size,
                center = Offset(
                    x = animatedX * size.width,
                    y = animatedY * size.height
                )
            )
        }
    }
}

fun slideInFromTop(): EnterTransition {
    return slideInVertically(
        initialOffsetY = { -it },
        animationSpec = tween(800, easing = FastOutSlowInEasing)
    )
}

fun slideInFromBottom(): EnterTransition {
    return slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(1000, easing = FastOutSlowInEasing)
    )
}

data class Cloud(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float
)

data class Sparkle(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val color: Color
)

