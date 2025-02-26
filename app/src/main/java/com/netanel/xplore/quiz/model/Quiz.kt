package com.netanel.xplore.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class Quiz(
    var questions: List<Question>,
    val timer: Int,
    val title: String? = null,
    val description: String? = null,
    val difficulty: DifficultyLevel? = null,
    val creatorId: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val maxParticipants: Int? = null,
    val tags: List<String>? = null,
    val isActive: Boolean = true,
    var totalScore: Int = 0
)