package com.ukyo.kukutrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ukyo.kukutrainer.shared.MultiplicationQuestion
import com.ukyo.kukutrainer.shared.MultiplicationTrainer
import com.ukyo.kukutrainer.shared.TrainerState
import com.ukyo.kukutrainer.ui.theme.KukuTrainerTheme

class MainActivity : ComponentActivity() {
    private val trainer = MultiplicationTrainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KukuTrainerTheme {
                TrainerScreen(trainer = trainer)
            }
        }
    }
}

@Composable
private fun TrainerScreen(trainer: MultiplicationTrainer) {
    val state by trainer.observeState().collectAsState(initial = TrainerState())
    var stage by remember { mutableIntStateOf(state.activeStage) }

    LaunchedEffect(stage) {
        trainer.loadStage(stage)
    }

    Scaffold { padding ->
        TrainerContent(
            state = state,
            onAdvanceStage = {
                stage = if (stage >= 9) 1 else stage + 1
            },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
private fun TrainerContent(
    state: TrainerState,
    onAdvanceStage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "ステージ ${state.activeStage} の九九",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "スター: ${state.stars}",
            style = MaterialTheme.typography.titleMedium
        )
        if (state.questions.isNotEmpty()) {
            QuestionList(questions = state.questions)
        } else {
            Text(text = "問題を読み込んでいます…", style = MaterialTheme.typography.bodyLarge)
        }
        Button(onClick = onAdvanceStage) {
            Text(text = "つぎのステージ")
        }
        if (state.completed) {
            Text(text = "全問クリア！", style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun QuestionList(questions: List<MultiplicationQuestion>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(questions) { question ->
            QuestionRow(question)
        }
    }
}

@Composable
private fun QuestionRow(question: MultiplicationQuestion) {
    Text(
        text = "${question.left} × ${question.right} = ${question.answer}",
        style = MaterialTheme.typography.bodyLarge
    )
}
