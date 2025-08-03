package com.example.kukutrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.ui.theme.KukuTrainerTheme

class CharacterSelectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KukuTrainerTheme {
                CharacterSelectScreen { id ->
                    PreferencesManager.setSelectedCharacter(this, id)
                    finish()
                }
            }
        }
    }
}