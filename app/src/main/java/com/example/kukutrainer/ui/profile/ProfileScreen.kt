package com.example.kukutrainer.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kukutrainer.R
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.navigation.Screen

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

    val highestStage = (1..9).lastOrNull { PreferencesManager.isStageCompleted(context, it) } ?: 0
    val stageText = if (highestStage > 0) "${highestStage}だん" else "まだ"

    val quizText = when {
        PreferencesManager.isQuizCompleted(context, 2) -> "むずかしい"
        PreferencesManager.isQuizCompleted(context, 1) -> "かんたん"
        else -> "まだ"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "プロフィール",
            style = MaterialTheme.typography.headlineMedium
        )
        characterInfo?.let { (name, imageRes) ->
            Image(
                painter = painterResource(imageRes),
                contentDescription = name,
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Text(
            text = "がくしゅうしただん: $stageText",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "クイズで正解した難易度: $quizText",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { navController.navigate(Screen.Home.route) }) {
            Text("ホームへ")
        }
    }
}

