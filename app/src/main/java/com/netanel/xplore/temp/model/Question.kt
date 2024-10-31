package com.netanel.xplore.temp.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Int,
    val text: String,
    val answers: List<String>,
    val correctAnswerIndex: Int,
    val type: QuestionType
)

enum class QuestionType {
    American,
    TrueOrFalse
}
