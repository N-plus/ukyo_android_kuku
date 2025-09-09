@file:OptIn(ExperimentalMaterial3Api::class)

package com.ukyo.kukutrainer.ui.quiz

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.ukyo.kukutrainer.audio.withClickSound

@Composable
fun QuizResultScreen(
    correctAnswers: Int = 8,
    totalQuestions: Int = 10,
    quizDurationMillis: Long = 0L,
    onRetry: () -> Unit = {},
    onHome: () -> Unit = {},
    onNextStage: () -> Unit = {}
) {
    var isVisible by remember { mutableStateOf(false) }
    var showConfetti by remember { mutableStateOf(false) }

// クイズにかかった時間を整形
    val quizTime = remember(quizDurationMillis) {
        val totalSeconds = quizDurationMillis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        if (minutes > 0) String.format("%d分%d秒", minutes, seconds)
        else String.format("%d秒", seconds)
    }

    // 正解率を計算
    val scorePercentage = (correctAnswers.toFloat() / totalQuestions * 100).toInt()
    val isPerfectScore = correctAnswers == totalQuestions
    val isGoodScore = correctAnswers >= 8
    val isOkayScore = correctAnswers >= 6

    // スコアに応じたメッセージと色
    val (message, emoji, mainColor, celebrationEmoji) = when {
        isPerfectScore -> Tuple4(
            "パーフェクト！\nすごいすごい！！",
            "🎉",
            Color(0xFFFFD700), // ゴールド
            listOf("🌸", "💮", "🌺", "🌼", "💐")
        )
        isGoodScore -> Tuple4(
            "とってもよくできました！\nすばらしい！",
            "😊",
            Color(0xFF4ECDC4), // ターコイズ
            listOf("😊", "👍", "💖", "🌈", "⭐")
        )
        isOkayScore -> Tuple4(
            "よくがんばりました！\nもうすこし！",
            "😄",
            Color(0xFFFFE66D), // イエロー
            listOf("😄", "💪", "🌻", "👌", "🎈")
        )
        else -> Tuple4(
            "もういちどちょうせん\nしてみよう！",
            "😅",
            Color(0xFFFF8A80), // ライトレッド
            listOf("😅", "💪", "🔥", "📚", "✊")
        )
    }

    // 背景グラデーション
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            mainColor.copy(alpha = 0.1f),
            Color(0xFFF8BBD9),
            Color(0xFFE8F5E8),
            Color(0xFFFFE5B4)
        )
    )

    // 紙吹雪アニメーション
    LaunchedEffect(Unit) {
        delay(500)
        isVisible = true
        if (isPerfectScore) {
            delay(1000)
            showConfetti = true
        }
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // メインリザルト表示
            AnimatedVisibility(
                visible = isVisible,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        mainColor.copy(alpha = 0.1f),
                                        Color.White
                                    )
                                )
                            )
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 結果タイトル
                        Text(
                            text = "クイズおつかれさま！",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3436),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // スコア表示
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // 正解数
                            Text(
                                text = correctAnswers.toString(),
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Bold,
                                color = mainColor,
                                modifier = Modifier
                                    .background(
                                        color = mainColor.copy(alpha = 0.1f),
                                        shape = CircleShape
                                    )
                                    .padding(16.dp)
                            )

                            Text(
                                text = " / ",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3436)
                            )

                            // 総問題数
                            Text(
                                text = totalQuestions.toString(),
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF636E72)
                            )

                            Text(
                                text = "もん",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF636E72)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // パーセンテージ
                        Text(
                            text = "${scorePercentage}てん！",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = mainColor,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // メッセージとキャラクター
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = emoji,
                                fontSize = 64.sp
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = message,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2D3436),
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // クイズにかかった時間を表示
                        Text(
                            text = "クイズにかかった時間: $quizTime",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2D3436)
                        )
                    }
                }
            }

            // ボタンエリア
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { 100 },
                    animationSpec = tween(800, delayMillis = 1000)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // パーフェクトスコアの場合は次のステージボタン
                    if (isPerfectScore) {
                        Button(
                            onClick = withClickSound(onNextStage),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .shadow(8.dp, RoundedCornerShape(28.dp)),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFD700)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.NavigateNext,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "つぎのむずかしさにすすむ！",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // もう一度ボタン
                        if (!isPerfectScore) {
                            Button(
                                onClick = withClickSound(onRetry),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                                    .shadow(6.dp, RoundedCornerShape(28.dp)),
                                shape = RoundedCornerShape(28.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = mainColor
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "もういちど",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // ホームボタン
                        Button(
                            onClick = withClickSound(onHome),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .shadow(6.dp, RoundedCornerShape(28.dp)),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF636E72)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ホーム",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // 紙吹雪エフェクト（パーフェクトスコア時）
        if (showConfetti && isPerfectScore) {
            ConfettiEffect(celebrationEmoji)
        }
    }
}

@Composable
fun QuestionResultCircle(
    questionNumber: Int,
    isCorrect: Boolean,
    delay: Long
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delay)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy
            )
        )
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFFF5252),
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCorrect) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "正解",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "不正解",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ConfettiEffect(emojis: List<String>) {
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")

    // 複数の紙吹雪を配置
    repeat(15) { index ->
        val randomEmoji = emojis.random()
        val randomX = (0..100).random()
        val randomDelay = (index * 200).toLong()

        val animatedY by infiniteTransition.animateFloat(
            initialValue = -100f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 3000,
                    delayMillis = randomDelay.toInt(),
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "confetti_y_$index"
        )

        val animatedRotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "confetti_rotation_$index"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = randomEmoji,
                fontSize = 24.sp,
                modifier = Modifier
                    .offset(
                        x = (randomX).dp,
                        y = animatedY.dp
                    )
                    .rotate(animatedRotation)
            )
        }
    }
}

// データクラスのヘルパー
data class Tuple4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Preview(showBackground = true)
@Composable
fun QuizResultScreenPreview() {
    QuizResultScreen(
        correctAnswers = 10,
        totalQuestions = 10,
        quizDurationMillis = 75_000L,
        onRetry = {},
        onHome = {},
        onNextStage = {}
    )
}