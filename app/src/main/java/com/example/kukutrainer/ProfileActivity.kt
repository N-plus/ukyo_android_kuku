package com.example.kukutrainer

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kukutrainer.data.PreferencesManager

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val stars = PreferencesManager.getStarCount(this, 3)
        findViewById<TextView>(R.id.text_star_count).text = "Stars: $stars"
    }

    override fun onResume() {
        super.onResume()
        val stars = PreferencesManager.getStarCount(this, 3)
        findViewById<TextView>(R.id.text_star_count).text = "Stars: $stars"
    }
}