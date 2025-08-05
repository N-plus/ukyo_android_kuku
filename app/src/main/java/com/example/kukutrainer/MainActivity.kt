package com.example.kukutrainer

import KukuNavGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.kukutrainer.audio.BgmPlayer
import com.example.kukutrainer.ui.theme.KukuTrainerTheme

class MainActivity : ComponentActivity() {
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
    }

    override fun onResume() {
        super.onResume()
        BgmPlayer.start(this)
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