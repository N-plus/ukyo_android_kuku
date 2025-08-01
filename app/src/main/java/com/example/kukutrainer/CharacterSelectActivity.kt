package com.example.kukutrainer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class CharacterSelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_select)

        findViewById<Button>(R.id.button_to_learning_stage).setOnClickListener {
            startActivity(Intent(this, LearningStageSelectActivity::class.java))
        }
    }
}