package com.example.kukutrainer.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kukutrainer.R
import com.example.kukutrainer.data.PreferencesManager

@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val totalStars = remember { (1..9).sumOf { PreferencesManager.getStarCount(context, it) } }
    val selectedCharacter = remember { PreferencesManager.getSelectedCharacter(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(id = R.string.profile_title), style = MaterialTheme.typography.headlineLarge)

        Text(text = stringResource(id = R.string.profile_total_stars, totalStars))

        Text(text = stringResource(id = R.string.profile_badges))
        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.height(120.dp)) {
            items((1..9).toList()) { stage ->
                val completed = PreferencesManager.isStageCompleted(context, stage)
                val color = if (completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(40.dp)
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stage.toString())
                }
            }
        }

        val charLabel = when (selectedCharacter) {
            1 -> stringResource(id = R.string.character_1)
            2 -> stringResource(id = R.string.character_2)
            3 -> stringResource(id = R.string.character_3)
            else -> stringResource(id = R.string.character_1)
        }
        Text(text = stringResource(id = R.string.profile_selected_character, charLabel))

        Spacer(modifier = Modifier.weight(1f))

        Text(text = stringResource(id = R.string.dressup_placeholder))
    }
}