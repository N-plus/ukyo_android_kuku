package com.example.kukutrainer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.kukutrainer.data.PreferencesManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            val hasChar = PreferencesManager.getSelectedCharacter(this) != 0
            val next = if (hasChar) {
                MainMenuActivity::class.java
            } else {
                CharacterSelectActivity::class.java
            }
            startActivity(Intent(this, next))
            finish()
        }, 3000)
    }
}