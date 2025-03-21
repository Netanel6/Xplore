package com.netanel.xplore.quiz.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateScoreRequest(
    val userId: String,
    val score: Int?
)