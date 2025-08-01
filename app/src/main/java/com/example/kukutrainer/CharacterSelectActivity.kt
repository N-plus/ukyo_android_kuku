package com.example.kukutrainer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kukutrainer.ui.theme.KukuTrainerTheme
import com.example.kukutrainer.saveSelectedCharacter

class CharacterSelectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KukuTrainerTheme {
                CharacterSelectScreen { id ->
                    saveSelectedCharacter(id)
                    startActivity(Intent(this, MainMenuActivity::class.java))
                    finish()
                }
            }
        }
    }
}