@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.kukutrainer.ui.quiz

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kukutrainer.navigation.Screen
import kotlinx.coroutines.delay

enum class QuizDifficulty(
    val title: String,
    val description: String,
    val color: Color,
    val secondaryColor: Color,
    val emoji: String
) {
    EASY("かんたん", "こたえを４つからえらぶよ", Color(0xFF4CAF50), Color(0xFF81C784), "🐣"),
    HARD("むずかしい", "こたえをにゅうりょくするよ", Color(0xFFFF9800), Color(0xFFFFB74D), "🦁")
}

@Composable
fun QuizDifficultySelectScreen(navController: NavHostController) {
    QuizDifficultySelectScreen(
        onDifficultySelected = { difficulty ->
            val diff = when (difficulty) {
                QuizDifficulty.EASY -> 1
                QuizDifficulty.HARD -> 2
            }
            navController.navigate(Screen.Quiz.createRoute(diff))
        },
        onBackPressed = { navController.popBackStack() }
    )
}

@Composable
fun QuizDifficultySelectScreen(
    onDifficultySelected: (QuizDifficulty) -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE1F5FE),
                        Color(0xFFF3E5F5),
                        Color(0xFFFFF3E0)
                    )
                )
            )
    ) {
        BackgroundDecorations(rotation = rotation)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Title()

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                QuizDifficulty.values().forEach { difficulty ->
                    DifficultyButton(
                        difficulty = difficulty,
                        onClick = { onDifficultySelected(difficulty) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            BackButton(onClick = onBackPressed)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun BackgroundDecorations(rotation: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFFFD700).copy(alpha = 0.3f),
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.TopStart)
                .rotate(rotation / 2)
        )

        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFFF69B4).copy(alpha = 0.3f),
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.TopEnd)
                .rotate(-rotation / 3)
        )

        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFF00CED1).copy(alpha = 0.3f),
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.BottomStart)
                .rotate(rotation / 4)
        )

        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFF98FB98).copy(alpha = 0.3f),
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.BottomEnd)
                .rotate(-rotation / 2)
        )
    }
}

@Composable
private fun Title() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "🎮",
            fontSize = 48.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "どのレベルで\nあそぶ？",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color(0xFF37474F),
            lineHeight = 36.sp
        )
    }
}

@Composable
private fun DifficultyButton(
    difficulty: QuizDifficulty,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .scale(scale)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = difficulty.color.copy(alpha = 0.25f)
            )
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            difficulty.color.copy(alpha = 0.1f),
                            difficulty.secondaryColor.copy(alpha = 0.05f)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = difficulty.emoji,
                    fontSize = 40.sp,
                    modifier = Modifier.padding(end = 20.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = difficulty.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = difficulty.color
                    )

                    Text(
                        text = difficulty.description,
                        fontSize = 16.sp,
                        color = Color(0xFF757575),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    difficulty.color.copy(alpha = 0.2f),
                                    difficulty.color.copy(alpha = 0.1f)
                                )
                            ),
                            shape = CircleShape
                        )
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "→",
                        fontSize = 24.sp,
                        color = difficulty.color,
                        fontWeight = FontWeight.Bold
                    )
                }
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
private fun BackButton(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .scale(scale)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "←",
                fontSize = 20.sp,
                color = Color(0xFF757575),
                modifier = Modifier.padding(end = 8.dp)
            )

            Text(
                text = "もどる",
                fontSize = 18.sp,
                color = Color(0xFF757575),
                fontWeight = FontWeight.Medium
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizDifficultySelectScreenPreview() {
    MaterialTheme {
        QuizDifficultySelectScreen()
    }
}

