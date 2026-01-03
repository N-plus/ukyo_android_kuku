package com.ukyo.kukutrainer.shared

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class MultiplicationTrainer(
    private val logger: PlatformLogger = PlatformLogger()
) {
    private val state = MutableStateFlow(TrainerState())

    fun observeState(): Flow<TrainerState> = state

    fun loadStage(stage: Int) {
        require(stage in 1..9) { "Stage must be between 1 and 9" }
        val questions = buildQuestions(stage)
        logger.log("Trainer", "Loaded stage $stage with ${questions.size} questions")
        state.update { current ->
            current.copy(
                activeStage = stage,
                questions = questions,
                stars = 0,
                completed = false
            )
        }
    }

    fun submitAnswer(question: MultiplicationQuestion, answer: Int) {
        val correct = question.answer == answer
        logger.log("Trainer", "Answer for ${question.left} x ${question.right}: $answer (correct=$correct)")
        state.update { current ->
            val newStars = if (correct) current.stars + 1 else current.stars
            current.copy(stars = newStars, completed = correct && newStars >= current.questions.size)
        }
    }

    private fun buildQuestions(stage: Int): List<MultiplicationQuestion> {
        val rightNumbers = (1..9).shuffled(Random(stage))
        return rightNumbers.map { right ->
            MultiplicationQuestion(left = stage, right = right)
        }
    }
}

data class MultiplicationQuestion(
    val left: Int,
    val right: Int
) {
    val answer: Int = left * right
}

data class TrainerState(
    val activeStage: Int = 1,
    val questions: List<MultiplicationQuestion> = emptyList(),
    val stars: Int = 0,
    val completed: Boolean = false
)
