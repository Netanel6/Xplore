package com.netanel.xplore.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Int,
    val text: String,
    val media: String,
    val answers: List<String>,
    val correctAnswerIndex: Int,
    val points: Int,
    val type: QuestionType
)

enum class QuestionType {
    American,
    TrueOrFalse
}
