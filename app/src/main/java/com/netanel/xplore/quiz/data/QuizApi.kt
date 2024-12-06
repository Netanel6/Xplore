package com.netanel.xplore.quiz.data

import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.utils.ServerResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface QuizApi {
    @GET("/questions")
    suspend fun getQuestions(): ServerResponse<List<Question>>

    @GET("/quiz/{quizId}")
    suspend fun getQuiz(@Path("quizId") quizId: String): ServerResponse<Quiz>

}