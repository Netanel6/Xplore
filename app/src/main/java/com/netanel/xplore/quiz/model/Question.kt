package com.netanel.xplore.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    var id: Int? = null,
    val media: String? = null,
    val text: String? = null,
    val answers: List<String>? = null,
    val explanation: String? = null,
    val correctAnswerIndex: Int? = null,
    val points: Int,
    val type: QuestionType? = null,
    val difficulty: DifficultyLevel,
    var userSelectedAnswer: Int? = null,
    var isAnswered: Boolean = false,
    var pointsGained: Int = 0,
    var isCorrect: Boolean = false
) {
    @Serializable
    enum class QuestionType {
        American, TrueOrFalse
    }
    @Serializable
    enum class DifficultyLevel {
        Easy, Medium, Hard
    }
}