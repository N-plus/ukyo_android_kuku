package com.example.kukutrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kukutrainer.ui.theme.KukuTrainerTheme

class QuizActivity : ComponentActivity() {

    companion object {
        const val EXTRA_DIFFICULTY = "extra_difficulty"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val difficulty = intent.getStringExtra(EXTRA_DIFFICULTY)
        setContent {
            KukuTrainerTheme {
                QuizScreen(difficulty)
            }
        }
    }
}

private const val DIFFICULTY_EASY = "easy"
private const val DIFFICULTY_HARD = "hard"

@Composable
private fun QuizScreen(difficulty: String?) {
    var hardestStatus by remember { mutableStateOf("難問中の難問: 未終了") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(hardestStatus)
            if (difficulty == DIFFICULTY_HARD) {
                HardLayout { hardestStatus = "難問中の難問: 終了" }
            } else {
                EasyLayout { hardestStatus = "難問中の難問: 終了" }
            }
        }
    }
}

@Composable
private fun EasyLayout(onOptionSelected: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("問題: ?")
        Button(onClick = onOptionSelected) { Text("選択肢1") }
        Button(onClick = onOptionSelected) { Text("選択肢2") }
        Button(onClick = onOptionSelected) { Text("選択肢3") }
    }
}

@Composable
private fun HardLayout(onSubmit: () -> Unit) {
    var answer by remember { mutableStateOf("") }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("問題: ?")
        TextField(
            value = answer,
            onValueChange = { answer = it },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = onSubmit) { Text("回答") }
    }
}