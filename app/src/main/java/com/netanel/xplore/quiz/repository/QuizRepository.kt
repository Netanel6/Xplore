package com.netanel.xplore.quiz.repository

import com.netanel.xplore.quiz.data.QuizApi
import com.netanel.xplore.quiz.model.Question
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class QuizRepository @Inject constructor(val api: QuizApi) {

    suspend fun getQuestions(): List<Question> = api.getQuestions()

}