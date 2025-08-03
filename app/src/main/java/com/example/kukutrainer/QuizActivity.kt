package com.example.kukutrainer

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DIFFICULTY = "extra_difficulty"
        private const val DIFFICULTY_EASY = "easy"
        private const val DIFFICULTY_HARD = "hard"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val difficulty = intent.getStringExtra(EXTRA_DIFFICULTY)
        val hardestStatus = findViewById<TextView>(R.id.hardest_status)

        val easyLayout = findViewById<View>(R.id.easy_layout)
        val hardLayout = findViewById<View>(R.id.hard_layout)

        if (difficulty == DIFFICULTY_HARD) {
            hardLayout.visibility = View.VISIBLE
            easyLayout.visibility = View.GONE

            val submitButton = findViewById<Button>(R.id.submit_button)
            submitButton.setOnClickListener {
                hardestStatus.text = "難問中の難問: 終了"
            }
        } else {
            easyLayout.visibility = View.VISIBLE
            hardLayout.visibility = View.GONE

            val option1 = findViewById<Button>(R.id.option1)
            val option2 = findViewById<Button>(R.id.option2)
            val option3 = findViewById<Button>(R.id.option3)

            val listener = View.OnClickListener {
                hardestStatus.text = "難問中の難問: 終了"
            }

            option1.setOnClickListener(listener)
            option2.setOnClickListener(listener)
            option3.setOnClickListener(listener)
        }
    }
}