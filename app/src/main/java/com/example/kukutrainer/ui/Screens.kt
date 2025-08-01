package com.example.kukutrainer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import com.example.kukutrainer.navigation.Screen

@Composable
fun SplashScreen(onFinished: (Boolean) -> Unit) {
    LaunchedEffect(Unit) {
        delay(1000)
        val selected = isCharacterSelected()
        onFinished(selected)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Splash")
    }
}

private fun isCharacterSelected(): Boolean {
    // placeholder for state reading. always false for now
    return false
}

@Composable
fun CharacterSelectionScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { navController.navigate(Screen.Home.route) }) {
            Text("Select Character")
        }
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