package com.example.kukutrainer.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kukutrainer.R
import com.example.kukutrainer.navigation.Screen

@Composable
fun QuizDifficultySelectScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { navController.navigate(Screen.Quiz.createRoute(1)) }) {
                Text(text = stringResource(id = R.string.easy))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate(Screen.Quiz.createRoute(3)) }) {
                Text(text = stringResource(id = R.string.hard))
            }
        }
    }
}