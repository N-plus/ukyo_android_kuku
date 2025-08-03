package com.example.kukutrainer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class QuizDifficultySelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_difficulty_select)

        findViewById<Button>(R.id.button_easy).setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra(QuizActivity.EXTRA_DIFFICULTY, "easy")
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_hard).setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra(QuizActivity.EXTRA_DIFFICULTY, "hard")
            startActivity(intent)
        }
    }
}