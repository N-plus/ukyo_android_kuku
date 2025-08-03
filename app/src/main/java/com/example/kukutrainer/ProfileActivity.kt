package com.example.kukutrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.ui.theme.KukuTrainerTheme

class ProfileActivity : ComponentActivity() {
    private var starCount by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        starCount = PreferencesManager.getStarCount(this, 3)
        setContent {
            KukuTrainerTheme {
                ProfileScreen(starCount)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        starCount = PreferencesManager.getStarCount(this, 3)
    }
}

@Composable
fun ProfileScreen(starCount: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Profile")
        Text(text = "Stars: $starCount")
    }
}
