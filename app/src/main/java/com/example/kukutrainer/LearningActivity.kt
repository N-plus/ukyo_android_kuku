package com.example.kukutrainer

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.kukutrainer.data.PreferencesManager

class LearningActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)

        findViewById<Button>(R.id.button_to_learning_stage).setOnClickListener {
            PreferencesManager.setStageCompleted(this, 3, true)
            PreferencesManager.setStarCount(this, 3, 3)
        }
    }
}