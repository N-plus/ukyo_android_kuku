package com.example.kukutrainer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class QuizDifficultySelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_difficulty_select)

        findViewById<Button>(R.id.button_to_quiz).setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }
    }
}