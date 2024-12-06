package com.netanel.xplore.quiz.repository

import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.quiz.model.Quiz

interface QuizRepository {
    suspend fun getQuestions(): List<Question>
    suspend fun getQuiz(quizId: String): Quiz
}