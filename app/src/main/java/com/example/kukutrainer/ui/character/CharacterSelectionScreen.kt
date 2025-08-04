package com.example.kukutrainer.ui.character

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun CharacterSelectionScreen(navController: NavHostController) {
    val context = LocalContext.current
    var selected = remember { mutableStateOf(PreferencesManager.getSelectedCharacter(context)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.select_character_prompt), style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        CharacterCard(
            label = stringResource(id = R.string.character_1),
            selected = selected.value == 1,
            onClick = {
                selected.value = 1
                PreferencesManager.setSelectedCharacter(context, 1)
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.CharacterSelection.route) { inclusive = true }
                }
            }
        )
        CharacterCard(
            label = stringResource(id = R.string.character_2),
            selected = selected.value == 2,
            onClick = {
                selected.value = 2
                PreferencesManager.setSelectedCharacter(context, 2)
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.CharacterSelection.route) { inclusive = true }
                }
            }
        )
        CharacterCard(
            label = stringResource(id = R.string.character_3),
            selected = selected.value == 3,
            onClick = {
                selected.value = 3
                PreferencesManager.setSelectedCharacter(context, 3)
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.CharacterSelection.route) { inclusive = true }
                }
            }
        )
    }
}

@Composable
private fun CharacterCard(label: String, selected: Boolean, onClick: () -> Unit) {
    val cardColors = if (selected) {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    } else {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    }
    val border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimary) else null
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        border = border,
        colors = cardColors
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(16.dp)
        )
    }
}