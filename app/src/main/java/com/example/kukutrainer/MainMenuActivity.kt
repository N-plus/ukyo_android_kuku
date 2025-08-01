package com.example.kukutrainer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.example.kukutrainer.data.PreferencesManager

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val characterId = PreferencesManager.getSelectedCharacter(this)
        findViewById<TextView>(R.id.text_selected_character).text = "Selected: $characterId"

        findViewById<Button>(R.id.button_character_select).setOnClickListener {
            startActivity(Intent(this, CharacterSelectActivity::class.java))
        }
        findViewById<Button>(R.id.button_quiz).setOnClickListener {
            startActivity(Intent(this, QuizDifficultySelectActivity::class.java))
        }
        findViewById<Button>(R.id.button_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        findViewById<Button>(R.id.button_profile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val characterId = PreferencesManager.getSelectedCharacter(this)
        findViewById<TextView>(R.id.text_selected_character).text = "Selected: $characterId"
    }
}