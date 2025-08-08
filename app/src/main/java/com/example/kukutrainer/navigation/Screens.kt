package com.example.kukutrainer.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object CharacterSelection : Screen("character_selection")
    data object Home : Screen("home")
    data object LearningStageSelect : Screen("learning_stage_select")
    data object Learning : Screen("learning/{stage}") {
        const val KEY_STAGE = "stage"
        fun createRoute(stage: Int) = "learning/$stage"
    }
    data object Completion : Screen("completion/{stage}") {
        const val KEY_STAGE = "stage"
        fun createRoute(stage: Int) = "completion/$stage"
    }
    data object QuizDifficultySelect : Screen("quiz_difficulty_select")
    data object Quiz : Screen("quiz/{difficulty}") {
        const val KEY_DIFFICULTY = "difficulty"
        fun createRoute(diff: Int) = "quiz/$diff"
    }
    data object QuizResult : Screen("quiz_result/{difficulty}/{correct}/{total}/{duration}") {
        const val KEY_DIFFICULTY = "difficulty"
        const val KEY_CORRECT = "correct"
        const val KEY_TOTAL = "total"
        const val KEY_DURATION = "duration"
        fun createRoute(diff: Int, correct: Int, total: Int, duration: Long) =
            "quiz_result/$diff/$correct/$total/$duration"
    }
    data object ForParents : Screen("for_parents")
    data object Settings : Screen("settings")
    data object Profile : Screen("profile")
    data object TermsOfService : Screen("terms_of_service")
}