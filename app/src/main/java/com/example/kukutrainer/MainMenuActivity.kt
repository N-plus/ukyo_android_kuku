package com.example.kukutrainer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.ui.theme.KukuTrainerTheme

class MainMenuActivity : ComponentActivity() {
    private var characterId by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        characterId = PreferencesManager.getSelectedCharacter(this)
        setContent {
            KukuTrainerTheme {
                MainMenuScreen(
                    characterId = characterId,
                    onSettings = { startActivity(Intent(this, SettingsActivity::class.java)) },
                    onCharacterSelect = { startActivity(Intent(this, CharacterSelectActivity::class.java)) },
                    onQuiz = { startActivity(Intent(this, QuizDifficultySelectActivity::class.java)) },
                    onProfile = { startActivity(Intent(this, ProfileActivity::class.java)) }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        characterId = PreferencesManager.getSelectedCharacter(this)
    }
}

@Composable
fun MainMenuScreen(
    characterId: Int,
    onSettings: () -> Unit,
    onCharacterSelect: () -> Unit,
    onQuiz: () -> Unit,
    onProfile: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Selected: $characterId")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onSettings) { Text("Settings") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onCharacterSelect) { Text("Character Select") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onQuiz) { Text("Quiz") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onProfile) { Text("Profile") }
    }
}