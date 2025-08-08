import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.kukutrainer.navigation.Screen
import com.example.kukutrainer.ui.home.HomeScreen
import com.example.kukutrainer.ui.splash.SplashScreen
import com.example.kukutrainer.ui.character.CharacterSelectionScreen
import com.example.kukutrainer.ui.learning.LearningStageSelectScreen
import com.example.kukutrainer.ui.learning.LearningScreen
import com.example.kukutrainer.ui.learning.CompletionScreen
import com.example.kukutrainer.ui.quiz.QuizDifficultySelectScreen
import com.example.kukutrainer.ui.quiz.QuizScreen
import com.example.kukutrainer.ui.settings.SettingsScreen
import com.example.kukutrainer.ui.quiz.QuizResultScreen
import com.example.kukutrainer.ui.profile.ProfileScreen
import com.example.kukutrainer.ui.terms.TermsOfServiceScreen
import androidx.compose.ui.platform.LocalContext
import com.example.kukutrainer.data.PreferencesManager
import com.example.kukutrainer.ui.parents.ForParentsScreen

@Composable
fun KukuNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(navController = navController, startDestination = startDestination) {
        addSplash(navController)

        composable(Screen.CharacterSelection.route) {
            CharacterSelectionScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.LearningStageSelect.route) {
            LearningStageSelectScreen(navController)
        }

        composable(
            route = Screen.Learning.route,
            arguments = listOf(
                navArgument(Screen.Learning.KEY_STAGE) {
                    type = NavType.IntType
                    defaultValue = 1
                }
            )
        ) { backStackEntry ->
            val stage = backStackEntry.arguments
                ?.getInt(Screen.Learning.KEY_STAGE) ?: 0
            LearningScreen(stage, navController)
        }

        composable(
            route = Screen.Completion.route,
            arguments = listOf(
                navArgument(Screen.Completion.KEY_STAGE) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val stage = backStackEntry.arguments
                ?.getInt(Screen.Completion.KEY_STAGE) ?: 0
            CompletionScreen(stage, navController)
        }

        composable(Screen.QuizDifficultySelect.route) {
            QuizDifficultySelectScreen(navController)
        }

        composable(
            route = Screen.Quiz.route,
            arguments = listOf(
                navArgument(Screen.Quiz.KEY_DIFFICULTY) {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val diff = backStackEntry.arguments
                ?.getInt(Screen.Quiz.KEY_DIFFICULTY) ?: 0
            QuizScreen(diff, navController)
        }
        composable(
            route = Screen.QuizResult.route,
            arguments = listOf(
                navArgument(Screen.QuizResult.KEY_DIFFICULTY) { type = NavType.IntType },
                navArgument(Screen.QuizResult.KEY_CORRECT) { type = NavType.IntType },
                navArgument(Screen.QuizResult.KEY_TOTAL) { type = NavType.IntType },
                navArgument(Screen.QuizResult.KEY_DURATION) { type = NavType.LongType }
                      )
        ) { backStackEntry ->
            val diff = backStackEntry.arguments?.getInt(Screen.QuizResult.KEY_DIFFICULTY) ?: 0
            val correct = backStackEntry.arguments?.getInt(Screen.QuizResult.KEY_CORRECT) ?: 0
            val total = backStackEntry.arguments?.getInt(Screen.QuizResult.KEY_TOTAL) ?: 0
            val duration = backStackEntry.arguments?.getLong(Screen.QuizResult.KEY_DURATION) ?: 0L
            QuizResultScreen(
                correctAnswers = correct,
                totalQuestions = total,
                quizDurationMillis = duration,
                onRetry = { navController.navigate(Screen.Quiz.createRoute(diff)) },
                onHome = { navController.navigate(Screen.Home.route) },
                onNextStage = { navController.navigate(Screen.Quiz.createRoute(diff + 1)) }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Screen.ForParents.route) {
            ForParentsScreen { navController.popBackStack() }
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(Screen.TermsOfService.route) {
            val context = LocalContext.current
            val fromSettings = navController.previousBackStackEntry?.destination?.route == Screen.Settings.route
            TermsOfServiceScreen(
                onAccept = {
                    PreferencesManager.setTermsAccepted(context, true)
                    if (fromSettings) {
                        navController.popBackStack()
                    } else {
                        val target = if (PreferencesManager.getSelectedCharacter(context) != 0) {
                            Screen.Home.route
                        } else {
                            Screen.CharacterSelection.route
                        }
                        navController.popBackStack()
                        navController.navigate(target)
                    }
                },
                onBack = { navController.popBackStack() },
                showBackButton = fromSettings
            )
        }
    }
}

private fun NavGraphBuilder.addSplash(navController: NavHostController) {
    composable(Screen.Splash.route) {
        val context = LocalContext.current
        SplashScreen { selected ->
            navController.popBackStack()
            if (PreferencesManager.isTermsAccepted(context)) {
                val target = if (selected) Screen.Home.route else Screen.CharacterSelection.route
                navController.navigate(target)
            } else {
                navController.navigate(Screen.TermsOfService.route)
            }
        }
    }
}
