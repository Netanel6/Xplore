package com.netanel.xplore.quiz.data

import com.netanel.xplore.quiz.model.Question
import retrofit2.http.GET

interface QuizApi {
    @GET("/questions")
    suspend fun getQuestions(): List<Question>
}