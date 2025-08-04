@file:OptIn(ExperimentalAnimationApi::class)

package com.example.kukutrainer.ui.learning

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
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
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.navigation.Screen
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class StageInfo(
    val id: Int,
    val subtitle: String,
    val colors: List<Color>
)

@Composable
fun LearningStageSelectScreen(navController: NavHostController) {
    val context = LocalContext.current

    var selectedStage by remember { mutableStateOf<Int?>(null) }
    var showContent by remember { mutableStateOf(false) }

    val stages = (1..9).map { stage ->
        val completed = PreferencesManager.isStageCompleted(context, stage)
        val stars = PreferencesManager.getStarCount(context, stage)
        val status = when {
            completed -> stringResource(id = R.string.learning_status_completed)
            stars > 0 -> stringResource(id = R.string.learning_status_in_progress)
            else -> stringResource(id = R.string.learning_status_not_started)
        }
        val subtitle = if (completed) "${stars}★" else status
        StageInfo(stage, subtitle, stageColors[(stage - 1) % stageColors.size])
    }

    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val backgroundOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "background_offset"
    )

    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFF8DC),
                        Color(0xFFFFE4E1),
                        Color(0xFFF0E68C)
                    ),
                    radius = 1200f
                )
            )
    ) {
        FloatingHearts(backgroundOffset)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AnimatedVisibility(
                visible = showContent,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(800, easing = FastOutSlowInEasing)
                ) + fadeIn()
            ) {
                StageSelectionHeader(onBackPressed = { navController.popBackStack() })
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.weight(1f)) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = showContent,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(1000, easing = FastOutSlowInEasing)
                    ) + fadeIn(),
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            start = 8.dp,
                            top = 8.dp,
                            end = 8.dp,
                            bottom = if (selectedStage != null) 96.dp else 8.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(stages) { index, stage ->
                            StageCard(
                                stage = stage,
                                isSelected = selectedStage == stage.id,
                                animationDelay = index * 100L,
                                onClick = { selectedStage = stage.id }
                            )
                        }
                    }
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = selectedStage != null,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(600, easing = FastOutSlowInEasing)
                    ) + fadeIn(),
                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(400)
                    ) + fadeOut(),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    val stage = selectedStage
                    if (stage != null) {
                        StageSelectionButton(stage = stage) {
                            navController.navigate(Screen.Learning.createRoute(stage))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StageSelectionHeader(onBackPressed: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FloatingActionButton(
            onClick = onBackPressed,
            modifier = Modifier.size(48.dp),
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White
        ) {
            Text("←", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "だんを えらぼう！",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            Text(
                text = "どの だんで まなぶ？",
                fontSize = 16.sp,
                color = Color(0xFF388E3C)
            )
        }
    }
}

@Composable
private fun StageCard(
    stage: StageInfo,
    isSelected: Boolean,
    animationDelay: Long,
    onClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.9f
            isSelected -> 1.1f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_scale"
    )

    val bounce by rememberInfiniteTransition(label = "bounce").animateFloat(
        initialValue = 0f,
        targetValue = if (isSelected) 10f else 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce_animation"
    )

    LaunchedEffect(Unit) {
        delay(animationDelay)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(
            animationSpec = tween(600, easing = FastOutSlowInEasing)
        ) + fadeIn()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f)
                .scale(scale)
                .offset(y = bounce.dp)
                .clickable {
                    isPressed = true
                    onClick()
                }
                .shadow(
                    elevation = if (isSelected) 12.dp else 6.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .then(
                    if (isSelected) {
                        Modifier.border(
                            4.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFFD700),
                                    Color(0xFFFF6B35),
                                    Color(0xFFFFD700)
                                )
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    } else Modifier
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(stage.colors),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.stage_format, stage.id),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stage.subtitle,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD700)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
private fun StageSelectionButton(stage: Int, onConfirm: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .scale(scale)
            .clickable {
                isPressed = true
                onConfirm()
            }
            .shadow(8.dp, RoundedCornerShape(25.dp)),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF4CAF50),
                            Color(0xFF8BC34A)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(id = R.string.stage_format, stage),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "タップして はじめよう",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
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
private fun FloatingHearts(offset: Float) {
    val hearts = remember {
        List(12) {
            FloatingHeart(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 15 + 10,
                speed = Random.nextFloat() * 2 + 0.5f
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        hearts.forEach { heart ->
            val animatedY = (heart.y + (sin(offset * 0.01f * heart.speed) * 0.1f)) % 1f
            val animatedX = (heart.x + (cos(offset * 0.015f * heart.speed) * 0.05f)) % 1f

            drawCircle(
                color = Color(0xFFFF69B4).copy(alpha = 0.3f),
                radius = heart.size,
                center = Offset(
                    x = animatedX * size.width,
                    y = animatedY * size.height
                )
            )
        }
    }
}

private data class FloatingHeart(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float
)

private val stageColors = listOf(
    listOf(Color(0xFFFFB6C1), Color(0xFFFF69B4)),
    listOf(Color(0xFFDEB887), Color(0xFFCD853F)),
    listOf(Color(0xFFFFE4B5), Color(0xFFFFA500)),
    listOf(Color(0xFF98FB98), Color(0xFF32CD32)),
    listOf(Color(0xFF87CEEB), Color(0xFF4682B4)),
    listOf(Color(0xFFFFD700), Color(0xFFFF8C00)),
    listOf(Color(0xFFF5F5F5), Color(0xFF696969)),
    listOf(Color(0xFFDA70D6), Color(0xFF9370DB)),
    listOf(Color(0xFFFFA07A), Color(0xFFFA8072))
)

