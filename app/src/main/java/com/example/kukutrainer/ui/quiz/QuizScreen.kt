@file:OptIn(ExperimentalAnimationApi::class)

package com.example.kukutrainer.ui.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kukutrainer.navigation.Screen
import kotlinx.coroutines.delay
import androidx.compose.animation.core.rememberInfiniteTransition

@Composable
fun QuizScreen(difficulty: Int, navController: NavHostController) {
    val range = when (difficulty) {
        1 -> 1..3
        2 -> 1..6
        else -> 1..9
    }

    var left by remember { mutableStateOf(range.random()) }
    var right by remember { mutableStateOf(range.random()) }
    var options by remember { mutableStateOf(generateOptions(left, right, range)) }
    var currentQuestion by remember { mutableStateOf(1) }
    val totalQuestions = 10
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var showFeedback by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    fun nextQuestion() {
        currentQuestion++
        left = range.random()
        right = range.random()
        options = generateOptions(left, right, range)
        selectedAnswer = null
    }

    val animatedGradient = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animatedGradient.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    val gradient = Brush.radialGradient(
        colors = listOf(
            Color(0xFFFFE0F7),
            Color(0xFFE0F2FF),
            Color(0xFFF0FFE0),
            Color(0xFFFFF0E0)
        ),
        radius = 800f + animatedGradient.value * 200f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        BackgroundDecorations()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            QuizProgressSection(currentQuestion, totalQuestions)

            Spacer(modifier = Modifier.height(40.dp))

            QuestionSection(left, right)

            Spacer(modifier = Modifier.height(50.dp))

            AnswerOptionsSection(
                answers = options,
                selectedAnswer = selectedAnswer,
                onAnswerClick = { answer ->
                    selectedAnswer = answer
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    showFeedback = true
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            HomeButton(onClick = { navController.navigate(Screen.Home.route) })

            Spacer(modifier = Modifier.height(20.dp))
        }

        if (showFeedback) {
            FeedbackAnimation(
                isCorrect = selectedAnswer == left * right,
                onAnimationEnd = {
                    if (selectedAnswer == left * right) {
                        nextQuestion()
                    }
                    showFeedback = false
                }
            )
        }
    }
}

@Composable
fun BackgroundDecorations() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")

    val rotation1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation1"
    )

    val rotation2 by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation2"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val decorations = listOf(
        Triple(0.1f, 0.15f, Icons.Default.Star),
        Triple(0.9f, 0.2f, Icons.Default.Favorite),
        Triple(0.15f, 0.7f, Icons.Default.School),
        Triple(0.85f, 0.8f, Icons.Default.EmojiEvents),
        Triple(0.05f, 0.5f, Icons.Default.Cake),
        Triple(0.95f, 0.45f, Icons.Default.Diamond)
    )

    decorations.forEachIndexed { index, (x, y, icon) ->
        BoxWithConstraints {
            Box(
                modifier = Modifier
                    .offset(
                        x = maxWidth * x,
                        y = maxHeight * y
                    )
                    .rotate(if (index % 2 == 0) rotation1 else rotation2)
                    .scale(if (index % 3 == 0) scale else 1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.random().copy(alpha = 0.3f),
                    modifier = Modifier.size((25 + index * 5).dp)
                )
            }
        }
    }
}

@Composable
fun QuizProgressSection(current: Int, total: Int) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(800, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(800))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF4FC3F7),
                                Color(0xFF29B6F6),
                                Color(0xFF0288D1)
                            )
                        )
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Quiz,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "全${total}問中${current}問目",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionSection(left: Int, right: Int) {
    var questionVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(400)
        questionVisible = true
    }

    AnimatedVisibility(
        visible = questionVisible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(animationSpec = tween(600))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF3E0).copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NumberBubble(left.toString(), Color(0xFFFF6B9D))
                    Text(
                        text = "×",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4ECDC4)
                    )
                    NumberBubble(right.toString(), Color(0xFFFFE66D))
                    Text(
                        text = "=",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4ECDC4)
                    )
                    Text(
                        text = "?",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF95E1D3)
                    )
                }
            }
        }
    }
}

@Composable
fun NumberBubble(number: String, color: Color) {
    val bounce = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        bounce.animateTo(
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = Modifier
            .size(60.dp)
            .scale(bounce.value)
            .background(color = color, shape = CircleShape)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun AnswerOptionsSection(
    answers: List<Int>,
    selectedAnswer: Int?,
    onAnswerClick: (Int) -> Unit
) {
    var optionsVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(800)
        optionsVisible = true
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        answers.forEachIndexed { index, answer ->
            AnimatedVisibility(
                visible = optionsVisible,
                enter = slideInHorizontally(
                    initialOffsetX = { if (index % 2 == 0) -it else it },
                    animationSpec = tween(
                        durationMillis = 600,
                        delayMillis = index * 150,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(
                    animationSpec = tween(
                        durationMillis = 600,
                        delayMillis = index * 150
                    )
                )
            ) {
                AnswerButton(
                    answer = answer,
                    isSelected = selectedAnswer == answer,
                    onClick = { onAnswerClick(answer) }
                )
            }
        }
    }
}

@Composable
fun AnswerButton(
    answer: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "button_scale"
    )

    val colors = listOf(
        Color(0xFFFF6B9D) to Color(0xFFFF8A80),
        Color(0xFF4ECDC4) to Color(0xFF4DD0E1),
        Color(0xFFFFE66D) to Color(0xFFFFF176),
        Color(0xFF95E1D3) to Color(0xFFA7FFEB)
    )

    val colorIndex = answer % colors.size
    val (startColor, endColor) = colors[colorIndex]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .scale(scale)
            .clickable { onClick() },
        shape = RoundedCornerShape(35.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 2.dp else 8.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(startColor, endColor)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = answer.toString(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun HomeButton(onClick: () -> Unit) {
    var buttonVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1200)
        buttonVisible = true
    }

    AnimatedVisibility(
        visible = buttonVisible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(800, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(800))
    ) {
        Card(
            modifier = Modifier
                .clickable { onClick() }
                .padding(horizontal = 40.dp),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF8E24AA)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "ホームにもどる",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun FeedbackAnimation(
    isCorrect: Boolean,
    onAnimationEnd: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000)
        visible = false
        delay(300)
        onAnimationEnd()
    }

    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFFF5722)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                modifier = Modifier.size(120.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = if (isCorrect) "せいかい!" else "ざんねん!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
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

// Helper extension
fun Color.Companion.random(): Color {
    val colors = listOf(
        Color(0xFFFF6B9D),
        Color(0xFF4ECDC4),
        Color(0xFFFFE66D),
        Color(0xFF95E1D3),
        Color(0xFFFF8A80),
        Color(0xFF8BC34A),
        Color(0xFF9C27B0),
        Color(0xFFFF9800)
    )
    return colors.random()
}

