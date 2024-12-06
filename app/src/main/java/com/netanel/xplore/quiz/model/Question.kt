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
    val isMandatory: Boolean? = null
) {
    @Serializable
    enum class QuestionType {
        American, TrueOrFalse, FillInTheBlank
    }
}