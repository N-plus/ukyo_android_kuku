package com.example.kukutrainer.ui.profile

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kukutrainer.R
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.navigation.Screen
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.height
import android.widget.Toast

@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current

    val characterId = PreferencesManager.getSelectedCharacter(context)
    val characterInfo = when (characterId) {
        1 -> "ピカちゃん" to R.drawable.chara1
        2 -> "ミントちゃん" to R.drawable.chara2
        3 -> "サクラちゃん" to R.drawable.chara3
        else -> null
    }

    val badges = remember {
        (1..9).map { stage ->
            Badge("${stage}のだん", PreferencesManager.isStageCompleted(context, stage))
        }
    }

    val easyQuizCleared = PreferencesManager.isQuizCompleted(context, 1)
    val hardQuizCleared = PreferencesManager.isQuizCompleted(context, 2)

    val animatedVisibility = remember { mutableStateOf(false) }
    val bounceAnim by animateFloatAsState(
        targetValue = if (animatedVisibility.value) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = "bounce"
    )

    LaunchedEffect(Unit) {
        animatedVisibility.value = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),
                        Color(0xFFBBDEFB),
                        Color(0xFF90CAF9)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .scale(bounceAnim),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NameInputSection()

            Spacer(modifier = Modifier.height(16.dp))

            CharacterSection(characterInfo)

            Spacer(modifier = Modifier.height(16.dp))

            BadgeSection(badges)

            Spacer(modifier = Modifier.height(16.dp))

            QuizStatusSection(easyQuizCleared, hardQuizCleared)

            Spacer(modifier = Modifier.height(16.dp))

            StudyTimeSection()

            Spacer(modifier = Modifier.height(16.dp))

            HomeButton { navController.navigate(Screen.Home.route) }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun NameInputSection() {
    val context = LocalContext.current
    var name by remember { mutableStateOf(PreferencesManager.getUserName(context)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "きみのなまえをおしえてね",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    PreferencesManager.setUserName(context, name)
                    Toast.makeText(context, "とうろくできたよ", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text("とうろく")
            }
        }
    }
}

@Composable
private fun CharacterSection(info: Pair<String, Int>?) {
    if (info == null) return

    var isCharacterBouncing by remember { mutableStateOf(false) }
    val characterScale by animateFloatAsState(
        targetValue = if (isCharacterBouncing) 1.2f else 1f,
        animationSpec = spring(dampingRatio = 0.3f), label = "characterScale"
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            isCharacterBouncing = true
            delay(500)
            isCharacterBouncing = false
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(info.second),
                contentDescription = info.first,
                modifier = Modifier
                    .size(120.dp)
                    .scale(characterScale)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = info.first,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
        }
    }
}

@Composable
private fun BadgeSection(badges: List<Badge>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "🏆 がんばりバッジ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(badges) { badge ->
                    BadgeItem(badge = badge)
                }
            }
        }
    }
}

@Composable
private fun BadgeItem(badge: Badge) {
    var isGlowing by remember { mutableStateOf(false) }

    LaunchedEffect(badge.isEarned) {
        if (badge.isEarned) {
            while (true) {
                isGlowing = true
                delay(1000)
                isGlowing = false
                delay(1000)
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                if (badge.isEarned)
                    Color(0xFFFFD700).copy(alpha = if (isGlowing) 0.8f else 0.3f)
                else
                    Color.Gray.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = badge.name,
            tint = if (badge.isEarned) Color(0xFFFFD700) else Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = badge.name,
            fontSize = 10.sp,
            color = if (badge.isEarned) Color(0xFF1976D2) else Color.Gray,
            fontWeight = if (badge.isEarned) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun QuizStatusSection(easyCleared: Boolean, hardCleared: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "📝 クイズのせいせき",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuizStatusItem(
                    title = "かんたん",
                    isCleared = easyCleared,
                    emoji = "😊"
                )

                QuizStatusItem(
                    title = "むずかしい",
                    isCleared = hardCleared,
                    emoji = "🤔"
                )
            }
        }
    }
}

@Composable
private fun QuizStatusItem(title: String, isCleared: Boolean, emoji: String) {
    var isAnimating by remember { mutableStateOf(false) }

    LaunchedEffect(isCleared) {
        if (isCleared) {
            while (true) {
                isAnimating = true
                delay(2000)
                isAnimating = false
                delay(2000)
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                if (isCleared)
                    Color(0xFF4CAF50).copy(alpha = if (isAnimating) 0.8f else 0.3f)
                else
                    Color.Gray.copy(alpha = 0.3f),
                RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = emoji,
            fontSize = 32.sp
        )

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isCleared) Color(0xFF2E7D32) else Color.Gray
        )

        Text(
            text = if (isCleared) "クリア！" else "がんばろう！",
            fontSize = 12.sp,
            color = if (isCleared) Color(0xFF2E7D32) else Color.Gray
        )
    }
}

@Composable
private fun StudyTimeSection() {
    val context = LocalContext.current
    var totalMillis by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            val sessionStart = PreferencesManager.getSessionStartTime(context)
            val currentSession = if (sessionStart > 0L) {
                System.currentTimeMillis() - sessionStart
            } else {
                0L
            }
            totalMillis = PreferencesManager.getTotalStudyTime(context) + currentSession
            delay(1000)
        }
    }

    val hours = totalMillis / 3_600_000
    val minutes = (totalMillis / 60_000) % 60
    val seconds = (totalMillis / 1_000) % 60

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⏰ べんきょうじかん",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "${hours}じかん ${minutes}ぷん ${seconds}びょう",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )

            Text(
                text = "よくがんばってるね！",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

@Composable
private fun HomeButton(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.8f), label = "buttonScale"
    )

    Button(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .size(72.dp)
            .scale(buttonScale),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 4.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "ホーム",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

private data class Badge(
    val name: String,
    val isEarned: Boolean
)

