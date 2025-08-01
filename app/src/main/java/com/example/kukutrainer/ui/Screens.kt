package com.example.kukutrainer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import com.example.kukutrainer.navigation.Screen
import com.example.kukutrainer.R
import com.example.kukutrainer.data.PreferencesManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.ui.unit.dp
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.runtime.*

@Composable
fun SplashScreen(onFinished: (Boolean) -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        delay(3000)
        val selected = isCharacterSelected(context)
        onFinished(selected)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val context = LocalContext.current
            val drawable = AppCompatResources.getDrawable(context, R.mipmap.ic_launcher)
            if (drawable != null) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.splash_tagline))
        }
    }
}

private fun isCharacterSelected(context: Context): Boolean {
    return PreferencesManager.getSelectedCharacter(context) != 0
}


@Composable
fun CharacterSelectionScreen(navController: NavHostController) {
    val context = LocalContext.current
    var selected by remember { mutableStateOf(PreferencesManager.getSelectedCharacter(context)) }

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
            selected = selected == 1,
            onClick = {
                selected = 1
                PreferencesManager.setSelectedCharacter(context, 1)
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.CharacterSelection.route) { inclusive = true }
                }
            }
        )
        CharacterCard(
            label = stringResource(id = R.string.character_2),
            selected = selected == 2,
            onClick = {
                selected = 2
                PreferencesManager.setSelectedCharacter(context, 2)
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.CharacterSelection.route) { inclusive = true }
                }
            }
        )
        CharacterCard(
            label = stringResource(id = R.string.character_3),
            selected = selected == 3,
            onClick = {
                selected = 3
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
    val border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        border = border,
        colors = CardDefaults.cardColors()
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { navController.navigate(Screen.LearningStageSelect.route) }) {
            Text("Home")
        }
    }
}

@Composable
fun LearningStageSelectScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { navController.navigate(Screen.Learning.createRoute(1)) }) {
            Text("Stage Select")
        }
    }
}

@Composable
fun LearningScreen(stage: Int, navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { navController.navigate(Screen.Completion.route) }) {
            Text("Learning Stage $stage")
        }
    }
}

@Composable
fun CompletionScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { navController.navigate(Screen.Home.route) }) {
            Text("Completion")
        }
    }
}

@Composable
fun QuizDifficultySelectScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { navController.navigate(Screen.Quiz.createRoute(1)) }) {
            Text("Quiz Difficulty Select")
        }
    }
}

@Composable
fun QuizScreen(difficulty: Int, navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { navController.navigate(Screen.Completion.route) }) {
            Text("Quiz Difficulty $difficulty")
        }
    }
}

@Composable
fun SettingsScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Settings")
    }
}

@Composable
fun ProfileScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Profile")
    }
}