package com.netanel.xplore.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class Quiz(
    var _id: String = "",
    var questions: List<Question>,
    val quizTimer: Long,
    val answerLockTimer: Long,
    val title: String? = null,
    val creatorId: String? = null,
    val isActive: Boolean = true,
    var totalScore: Int = 0
)