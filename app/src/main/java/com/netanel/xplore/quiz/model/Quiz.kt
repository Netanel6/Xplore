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
    val totalScore: Int = 0,
    val currentScore: Int = 0,
    var scoreBoard: ScoreBoard
) {

    @Serializable
    data class ScoreBoard(val scores: List<Score>) {
        @Serializable
        data class Score(val id: String = "", val userName: String = "", val score: Int = 0)
    }
}