package com.netanel.xplore.quiz.repository

import com.netanel.xplore.quiz.model.Question

interface QuizRepository {
    suspend fun getQuestions(): List<Question>
}