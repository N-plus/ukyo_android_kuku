package com.example.kukutrainer.ui.learning

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kukutrainer.R
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.navigation.Screen

@Composable
fun LearningStageSelectScreen(navController: NavHostController) {
    val context = LocalContext.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items((1..9).toList()) { stage ->
            val completed = PreferencesManager.isStageCompleted(context, stage)
            val stars = PreferencesManager.getStarCount(context, stage)
            val status = when {
                completed -> stringResource(id = R.string.learning_status_completed)
                stars > 0 -> stringResource(id = R.string.learning_status_in_progress)
                else -> stringResource(id = R.string.learning_status_not_started)
            }
            val subtitle = if (completed) "${stars}★" else status
            StageCard(stage, subtitle) {
                navController.navigate(Screen.Learning.createRoute(stage))
            }
        }
    }
}

@Composable
private fun StageCard(stage: Int, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.stage_format, stage))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = subtitle)
        }
    }
}