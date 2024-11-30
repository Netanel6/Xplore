package com.netanel.xplore.quiz.data

import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.utils.ServerResponse
import retrofit2.http.GET

interface QuizApi {
    @GET("/questions")
    suspend fun getQuestions(): ServerResponse<List<Question>>

}