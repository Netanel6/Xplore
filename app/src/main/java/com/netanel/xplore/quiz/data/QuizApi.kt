package com.netanel.xplore.quiz.data

import com.netanel.xplore.auth.repository.model.User
import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.utils.ServerResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface QuizApi {
    @GET("/questions")
    suspend fun getQuestions(): ServerResponse<List<Question>>

    @GET("/quizzes/quiz/{quizId}")
    suspend fun getQuiz(@Path("quizId") quizId: String): ServerResponse<Quiz>

    @GET("/quizzes/all")
    suspend fun getQuizList(): ServerResponse<List<Quiz>>

    @GET("/quizzes/{userId}")
    suspend fun getUserQuizList(@Path("userId") userId: String): ServerResponse<List<User.Quiz>>
} 