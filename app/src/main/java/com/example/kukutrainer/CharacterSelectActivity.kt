package com.example.kukutrainer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.kukutrainer.data.PreferencesManager

class CharacterSelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_select)

        findViewById<View>(R.id.character1).setOnClickListener { selectCharacter(1) }
        findViewById<View>(R.id.character2).setOnClickListener { selectCharacter(2) }
        findViewById<View>(R.id.character3).setOnClickListener { selectCharacter(3) }
        findViewById<View>(R.id.character4).setOnClickListener { selectCharacter(4) }
    }

    private fun selectCharacter(id: Int) {
        PreferencesManager.setSelectedCharacter(this, id)
        finish()
    }
}
