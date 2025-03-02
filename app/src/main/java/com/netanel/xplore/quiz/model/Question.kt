package com.netanel.xplore.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    var id: Int? = null,
    val text: String? = null,
    val answers: List<String>? = null,
    val correctAnswerIndex: Int? = null,
    var userSelectedAnswer: Int? = null,
    var isAnswered: Boolean = false,
    var points: Int = 0,
    var isCorrect: Boolean = false
) {


    @Serializable
    enum class QuestionType {
        American, TrueOrFalse, FillInTheBlank
    }
}