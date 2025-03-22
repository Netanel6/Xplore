package com.netanel.xplore.quiz.repository

import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.quiz.model.UpdateScoreRequest

interface QuizRepository {
    suspend fun getQuestions(): List<Question>
    suspend fun getQuiz(quizId: String): Quiz
    suspend fun updateQuiz(quizId: String, score: UpdateScoreRequest): Quiz?
    suspend fun getQuizList(): List<Quiz>
    suspend fun getQuizListForUser(userId: String): List<Quiz>
}