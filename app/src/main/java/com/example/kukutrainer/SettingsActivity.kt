package com.example.kukutrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.kukutrainer.navigation.KukuNavGraph
import com.example.kukutrainer.navigation.Screen
import com.example.kukutrainer.ui.theme.KukuTrainerTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KukuTrainerTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    KukuNavGraph(navController, startDestination = Screen.Settings.route)
                }
            }
        }
    }
}