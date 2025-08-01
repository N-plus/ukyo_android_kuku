package com.example.kukutrainer

import android.os.Bundle
import android.widget.SeekBar
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.example.kukutrainer.data.PreferencesManager

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val bgmSwitch = findViewById<Switch>(R.id.switch_bgm)
        val speedSeek = findViewById<SeekBar>(R.id.seek_speed)

        bgmSwitch.isChecked = PreferencesManager.isBgmOn(this)
        speedSeek.progress = (PreferencesManager.getSoundSpeed(this) * 100).toInt()

        bgmSwitch.setOnCheckedChangeListener { _, isChecked ->
            PreferencesManager.setBgmOn(this, isChecked)
        }

        speedSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    PreferencesManager.setSoundSpeed(this@SettingsActivity, progress / 100f)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
