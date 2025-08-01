package com.example.kukutrainer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kukutrainer.ui.*

@Composable
fun KukuNavGraph(navController: NavHostController, startDestination: String = Screen.Splash.route) {
    NavHost(navController = navController, startDestination = startDestination) {
        addSplash(navController)
        composable(Screen.CharacterSelection.route) { CharacterSelectionScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.LearningStageSelect.route) { LearningStageSelectScreen(navController) }
        composable(Screen.Learning.route) { backStackEntry ->
            val stage = backStackEntry.arguments?.getString(Screen.Learning.KEY_STAGE)?.toIntOrNull() ?: 0
            LearningScreen(stage, navController)
        }
        composable(Screen.Completion.route) { CompletionScreen(navController) }
        composable(Screen.QuizDifficultySelect.route) { QuizDifficultySelectScreen(navController) }
        composable(Screen.Quiz.route) { backStackEntry ->
            val diff = backStackEntry.arguments?.getString(Screen.Quiz.KEY_DIFFICULTY)?.toIntOrNull() ?: 0
            QuizScreen(diff, navController)
        }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
    }
}

private fun NavGraphBuilder.addSplash(navController: NavHostController) {
    composable(Screen.Splash.route) {
        SplashScreen { selected ->
            val target = if (selected) Screen.Home.route else Screen.CharacterSelection.route
            navController.popBackStack()
            navController.navigate(target)
        }
    }
}