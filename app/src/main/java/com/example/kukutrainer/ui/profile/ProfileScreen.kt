@file:OptIn(ExperimentalAnimationApi::class)

package com.example.kukutrainer.ui.profile

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kukutrainer.data.PreferencesManager
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val totalStars = remember { (1..9).sumOf { PreferencesManager.getStarCount(context, it) } }
    val selectedCharacter = remember { PreferencesManager.getSelectedCharacter(context) }

    val animatedGradient = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedGradient.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFE5F1),
            Color(0xFFE5F3FF),
            Color(0xFFF0FFE5)
        ),
        startY = animatedGradient.value * 500f,
        endY = (animatedGradient.value + 1f) * 500f
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            AnimatedTitle()

            Spacer(modifier = Modifier.height(24.dp))

            StarsSection(totalStars)

            Spacer(modifier = Modifier.height(32.dp))

            BadgesSection(context)

            Spacer(modifier = Modifier.height(32.dp))

            SelectedCharacterSection(selectedCharacter)

            Spacer(modifier = Modifier.weight(1f))
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
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation1"
    )

    val rotation2 by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation2"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(x = 50.dp, y = 100.dp)
            .rotate(rotation1)
    ) {
        androidx.compose.material3.Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFFFD700).copy(alpha = 0.3f),
            modifier = Modifier.size(40.dp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(x = (-30).dp, y = 200.dp)
            .rotate(rotation2)
    ) {
        androidx.compose.material3.Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = Color(0xFFFF69B4).copy(alpha = 0.3f),
            modifier = Modifier.size(35.dp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(x = 80.dp, y = 400.dp)
            .rotate(-rotation1)
    ) {
        androidx.compose.material3.Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFF32CD32).copy(alpha = 0.3f),
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun AnimatedTitle() {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = slideInFromTop(
            animationSpec = tween(1000, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(1000))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF6B95FF),
                                Color(0xFF4ECDC4),
                                Color(0xFFFFE66D)
                            )
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "プロフィール",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun StarsSection(totalStars: Int) {
    var animateStars by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        animateStars = true
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF9C4).copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "これまで獲得した星: $totalStars",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(totalStars) { index ->
                    AnimatedStar(
                        delay = index * 100L,
                        animate = animateStars
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedStar(delay: Long, animate: Boolean) {
    var visible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "star_scale"
    )

    LaunchedEffect(animate) {
        if (animate) {
            delay(delay)
            visible = true
        }
    }

    androidx.compose.material3.Icon(
        imageVector = Icons.Default.Star,
        contentDescription = "獲得した星",
        tint = Color(0xFFFFD700),
        modifier = Modifier
            .size(40.dp)
            .scale(scale)
    )
}

@Composable
fun BadgesSection(context: Context) {
    var showBadges by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        showBadges = true
    }

    val badges = remember {
        val colors = listOf(
            Color(0xFF4CAF50),
            Color(0xFF2196F3),
            Color(0xFFFF9800),
            Color(0xFF9C27B0),
            Color(0xFFE91E63),
            Color(0xFF00BCD4)
        )
        val icons = listOf(
            Icons.Default.CheckCircle,
            Icons.Default.Star,
            Icons.Default.Favorite,
            Icons.Default.EmojiEvents,
            Icons.Default.Diamond,
            Icons.Default.MilitaryTech
        )
        (1..6).map { index ->
            BadgeInfo(
                number = index,
                color = colors[index - 1],
                icon = icons[index - 1],
                isUnlocked = PreferencesManager.isStageCompleted(context, index)
            )
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE1F5FE).copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "獲得バッジ",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            BadgeGrid(showBadges = showBadges, badges = badges)
        }
    }
}

@Composable
fun BadgeGrid(showBadges: Boolean, badges: List<BadgeInfo>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            badges.take(3).forEachIndexed { index, badge ->
                AnimatedBadge(
                    badge = badge,
                    delay = if (showBadges) index * 150L else 0L,
                    animate = showBadges
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            badges.drop(3).forEachIndexed { index, badge ->
                AnimatedBadge(
                    badge = badge,
                    delay = if (showBadges) (index + 3) * 150L else 0L,
                    animate = showBadges
                )
            }
        }
    }
}

data class BadgeInfo(
    val number: Int,
    val color: Color,
    val icon: ImageVector,
    val isUnlocked: Boolean
)

@Composable
fun AnimatedBadge(
    badge: BadgeInfo,
    delay: Long,
    animate: Boolean
) {
    var visible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "badge_scale"
    )

    LaunchedEffect(animate) {
        if (animate) {
            delay(delay)
            visible = true
        }
    }

    Box(
        modifier = Modifier
            .size(80.dp)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        if (badge.isUnlocked) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = badge.color
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = badge.icon,
                        contentDescription = "バッジ ${badge.number}",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = Color.Gray.copy(alpha = 0.3f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badge.number.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun SelectedCharacterSection(selectedCharacter: Int) {
    var showCharacter by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1500)
        showCharacter = true
    }

    AnimatedVisibility(
        visible = showCharacter,
        enter = slideInFromBottom(
            animationSpec = tween(800, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(800))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF3E5F5).copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "選出中キャラクター: キャラクター$selectedCharacter",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7B1FA2),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                CharacterAvatar()
            }
        }
    }
}

@Composable
fun CharacterAvatar() {
    val infiniteTransition = rememberInfiniteTransition(label = "character")
    val bounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Box(
        modifier = Modifier
            .size(120.dp)
            .offset(y = bounce.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFB74D)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "😊",
                    fontSize = 60.sp
                )
            }
        }
    }
}

fun slideInFromTop(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween()
) = slideInVertically(
    animationSpec = animationSpec,
    initialOffsetY = { -it }
)

fun slideInFromBottom(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween()
) = slideInVertically(
    animationSpec = animationSpec,
    initialOffsetY = { it }
)

