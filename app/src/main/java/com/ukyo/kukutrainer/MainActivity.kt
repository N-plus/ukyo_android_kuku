package com.ukyo.kukutrainer

import KukuNavGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.ukyo.kukutrainer.audio.BgmPlayer
import com.ukyo.kukutrainer.ui.theme.KukuTrainerTheme
import com.ukyo.kukutrainer.data.PreferencesManager

class MainActivity : ComponentActivity() {
    private var startTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BgmPlayer.start(this)
        setContent {
            KukuTrainerTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppContent()
                }
            }
        }
    }
    override fun onPause() {
        super.onPause()
        BgmPlayer.stop()
        val duration = System.currentTimeMillis() - startTime
        PreferencesManager.addStudyTime(this, duration)
    }

    override fun onResume() {
        super.onResume()
        BgmPlayer.start(this)
        startTime = System.currentTimeMillis()
        PreferencesManager.setSessionStartTime(this, startTime)
    }

    override fun onDestroy() {
        super.onDestroy()
        BgmPlayer.release()
    }
}

@Composable
fun AppContent() {
    val navController = rememberNavController()
    KukuNavGraph(navController)
}