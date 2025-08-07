@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.kukutrainer.ui.learning

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.navigation.Screen
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Data used to present completion details on the celebration screen.
 */
data class CompletionData(
    val categoryName: String,
    val emoji: String,
    val completedLessons: Int,
    val score: Int,
    val timeSpent: String,
    val newBadges: List<String> = emptyList()
)

/**
 * Entry point used by the navigation graph. It converts the app specific data to [CompletionData]
 * and wires the navigation callbacks to the fancy celebration UI.
 */
@Composable
fun CompletionScreen(stage: Int, navController: NavHostController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val stars = remember { PreferencesManager.getStarCount(context, stage) }

    val completionData = remember(stars, stage) {
        CompletionData(
            categoryName = "${stage}のスターゲット！",
            emoji = "⭐",
            completedLessons = stage,
            score = stars,
            timeSpent = "",
            newBadges = emptyList()
        )
    }

    CompletionContent(
        completionData = completionData,
        onNextLessonClicked = { navController.navigate(Screen.Learning.createRoute(stage + 1)) },
        onBackToMenuClicked = { navController.navigate(Screen.Home.route) }
    )
}

/**
 * Composable that renders the colourful completion screen as provided in the design.
 */
@Composable
fun CompletionContent(
    completionData: CompletionData,
    onNextLessonClicked: () -> Unit = {},
    onBackToMenuClicked: () -> Unit = {}
) {
    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "celebration")

    // "Congratulations" bounce animation
    val congratsScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "congrats_scale"
    )

    // Rotation animation
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing)
        ),
        label = "rotation"
    )

    // Colour change animation
    val celebrationColors = listOf(
        Color(0xFFFF6B9D), Color(0xFF4ECDC4), Color(0xFFFFE66D),
        Color(0xFF95E1D3), Color(0xFFFCE38A), Color(0xFFFF8A80)
    )

    var currentColorIndex by remember { mutableStateOf(0) }
    val currentColor by animateColorAsState(
        targetValue = celebrationColors[currentColorIndex],
        animationSpec = tween(1000),
        label = "color_change"
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentColorIndex = (currentColorIndex + 1) % celebrationColors.size
        }
    }

    // Slide in animation
    var isVisible by remember { mutableStateOf(false) }
    val slideIn by animateFloatAsState(
        targetValue = if (isVisible) 0f else 300f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "slide_in"
    )

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFE0F4),
                        Color(0xFFE1F5FE),
                        Color(0xFFF3E5F5)
                    )
                )
            )
    ) {
        // Background celebration effects
        CelebrationBackground(rotation = rotation)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .offset(y = slideIn.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Main celebration message
            CongratulationsMessage(
                scale = congratsScale,
                color = currentColor
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Achievement icon and category name
            AchievementDisplay(
                completionData = completionData,
                rotation = rotation
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Statistics cards
            StatsCards(completionData = completionData)

            Spacer(modifier = Modifier.height(32.dp))

            // New badge section if any
            if (completionData.newBadges.isNotEmpty()) {
                NewBadgesSection(badges = completionData.newBadges)
                Spacer(modifier = Modifier.height(24.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            // Action buttons
            ActionButtons(
                onNextLessonClicked = onNextLessonClicked,
                onBackToMenuClicked = onBackToMenuClicked
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CelebrationBackground(rotation: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val confettiColors = listOf(
            Color(0xFFFF6B9D), Color(0xFF4ECDC4), Color(0xFFFFE66D),
            Color(0xFF95E1D3), Color(0xFFFCE38A), Color(0xFFFF8A80)
        )

        repeat(30) { index ->
            val angle = (rotation + index * 12) * Math.PI / 180
            val radius = 100f + (index * 15f)
            val x = size.width / 2 + radius * cos(angle).toFloat()
            val y = size.height / 2 + radius * sin(angle).toFloat()

            if (x in 0f..size.width && y in 0f..size.height) {
                drawCircle(
                    color = confettiColors[index % confettiColors.size].copy(alpha = 0.6f),
                    radius = 8f + (index % 5) * 3f,
                    center = Offset(x, y)
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val celebrationEmojis = listOf("🎉", "🎊", "⭐", "🌟", "✨", "🏆", "🎈")

        celebrationEmojis.forEachIndexed { index, emoji ->
            val floatingOffset by rememberInfiniteTransition(label = "floating_$index").animateFloat(
                initialValue = 0f,
                targetValue = 20f,
                animationSpec = infiniteRepeatable(
                    animation = tween((2000 + index * 300), easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "floating_offset_$index"
            )

            Text(
                text = emoji,
                fontSize = (16 + index * 2).sp,
                modifier = Modifier
                    .align(
                        when (index % 4) {
                            0 -> Alignment.TopStart
                            1 -> Alignment.TopEnd
                            2 -> Alignment.BottomStart
                            else -> Alignment.BottomEnd
                        }
                    )
                    .padding(
                        start = if (index % 2 == 0) (30 + index * 20).dp else 0.dp,
                        end = if (index % 2 == 1) (30 + index * 20).dp else 0.dp,
                        top = if (index < 2) (80 + index * 30).dp else 0.dp,
                        bottom = if (index >= 2) (120 + index * 20).dp else 0.dp
                    )
                    .offset(y = floatingOffset.dp)
                    .rotate(rotation / (4 + index))
            )
        }
    }
}

@Composable
private fun CongratulationsMessage(scale: Float, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎊",
            fontSize = 64.sp,
            modifier = Modifier
                .scale(scale)
                .rotate(sin(scale * 10) * 10f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "おめでとう！",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            textAlign = TextAlign.Center,
            modifier = Modifier.scale(scale)
        )

        Text(
            text = "よくがんばったね！",
            fontSize = 20.sp,
            color = Color(0xFF37474F),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun AchievementDisplay(
    completionData: CompletionData,
    rotation: Float
) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .shadow(12.dp, CircleShape),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFD700).copy(alpha = 0.3f),
                            Color(0xFFFF8C00).copy(alpha = 0.1f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = completionData.emoji,
                    fontSize = 48.sp,
                    modifier = Modifier.rotate(rotation / 8)
                )
                Text(
                    text = completionData.categoryName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF37474F)
                )
            }
        }
    }
}

@Composable
private fun StatsCards(completionData: CompletionData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = "レッスン",
            value = "${completionData.completedLessons}のだん",
            subtitle = "かんりょう",
            emoji = "📚",
            color = Color(0xFF4CAF50),
            modifier = Modifier.weight(1f)
        )

    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    emoji: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            color.copy(alpha = 0.1f),
                            color.copy(alpha = 0.05f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = emoji,
                    fontSize = 24.sp
                )

                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )

                Text(
                    text = if (subtitle.isNotEmpty()) "$title$subtitle" else title,
                    fontSize = 10.sp,
                    color = Color(0xFF757575)
                )
            }
        }
    }
}

@Composable
private fun NewBadgesSection(badges: List<String>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "あたらしいバッジ！",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF37474F)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            badges.forEach { badge ->
                Card(
                    modifier = Modifier
                        .size(60.dp)
                        .shadow(4.dp, CircleShape),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFD700)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = badge,
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButtons(
    onNextLessonClicked: () -> Unit,
    onBackToMenuClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onNextLessonClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(8.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "つぎのレッスン",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "🚀",
                    fontSize = 20.sp
                )
            }
        }

        OutlinedButton(
            onClick = onBackToMenuClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF757575)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🏠", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "メニューにもどる",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun CompletionScreenPreview() {
    MaterialTheme {
        CompletionContent(
            completionData = CompletionData(
                categoryName = "ひらがな",
                emoji = "あ",
                completedLessons = 5,
                score = 95,
                timeSpent = "10分",
                newBadges = listOf("🏆", "⭐")
            )
        )
    }
}