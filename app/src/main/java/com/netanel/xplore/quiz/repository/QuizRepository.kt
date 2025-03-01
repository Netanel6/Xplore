package com.netanel.xplore.quiz.repository

import com.netanel.xplore.auth.repository.model.User
import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.utils.ServerResponse

interface QuizRepository {
    suspend fun getQuestions(): List<Question>
    suspend fun getQuiz(quizId: String): Quiz
    suspend fun getQuizList(): List<Quiz>
    suspend fun getQuizListForUser(userId: String): List<Quiz>
}