package com.example.kukutrainer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kukutrainer.ui.theme.KukuTrainerTheme

private const val DIFFICULTY_EASY = "easy"
private const val DIFFICULTY_HARD = "hard"

class QuizDifficultySelectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KukuTrainerTheme {
                QuizDifficultySelectScreen { difficulty ->
                    val intent = Intent(this, QuizActivity::class.java)
                    intent.putExtra(QuizActivity.EXTRA_DIFFICULTY, difficulty)
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
private fun QuizDifficultySelectScreen(onSelect: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("難易度を選択")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onSelect(DIFFICULTY_EASY) }) {
                Text("かんたん")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onSelect(DIFFICULTY_HARD) }) {
                Text("むずかしい")
            }
        }
    }
}
