package com.example.kukutrainer

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.example.kukutrainer.data.PreferencesManager

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val bgmSwitch = findViewById<Switch>(R.id.switch_bgm)
        val speedSwitch = findViewById<Switch>(R.id.switch_speed)
        val charButton = findViewById<Button>(R.id.button_character)

        bgmSwitch.isChecked = PreferencesManager.isBgmOn(this)
        speedSwitch.isChecked = PreferencesManager.getSoundSpeed(this) >= 1f
        updateSpeedLabel(speedSwitch)

        bgmSwitch.setOnCheckedChangeListener { _, isChecked ->
            PreferencesManager.setBgmOn(this, isChecked)
        }

        speedSwitch.setOnCheckedChangeListener { _, isChecked ->
            PreferencesManager.setSoundSpeed(this, if (isChecked) 1f else 0.5f)
            updateSpeedLabel(speedSwitch)
        }

        charButton.setOnClickListener {
            startActivity(Intent(this, CharacterSelectActivity::class.java))
        }
    }

    private fun updateSpeedLabel(switch: Switch) {
        val label = if (switch.isChecked) R.string.sound_speed_fast else R.string.sound_speed_slow
        switch.text = getString(label)
    }
}