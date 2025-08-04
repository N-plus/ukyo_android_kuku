package com.example.kukutrainer.ui.learning

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.kukutrainer.R
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.navigation.Screen
import com.example.kukutrainer.ui.FeedbackAnimation
import com.example.kukutrainer.ui.FeedbackState
import androidx.compose.ui.unit.dp

@Composable
fun CompletionScreen(stage: Int, navController: NavHostController) {
    val context = LocalContext.current
    val stars = remember { PreferencesManager.getStarCount(context, stage) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FeedbackAnimation(state = FeedbackState.Completed)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.congratulations))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${stars}★")
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { navController.navigate(Screen.Home.route) }) {
                Text(text = stringResource(id = R.string.back_to_home))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate(Screen.Learning.createRoute(stage + 1)) }) {
                Text(text = stringResource(id = R.string.next_stage))
            }
        }
    }
}