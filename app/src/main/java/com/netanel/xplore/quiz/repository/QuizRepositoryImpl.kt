package com.netanel.xplore.quiz.repository

import com.netanel.xplore.quiz.data.QuizApi
import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.quiz.model.UpdateScoreRequest


/**
 * Created by netanelamar on 30/11/2024.
 * NetanelCA2@gmail.com
 */
class QuizRepositoryImpl(private val api: QuizApi): QuizRepository {

    override suspend fun getQuestions(): List<Question> {
        val response = api.getQuestions()
        if (response.status == "success" && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message ?: "Unknown error")
        }
    }

    override suspend fun getQuiz(quizId: String): Quiz {
        val response = api.getQuiz(quizId)
        if (response.status == "success" && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message ?: "Unknown error")
        }
    }

    override suspend fun updateQuiz(quizId: String, score: UpdateScoreRequest): Quiz? {
        return try {
            val response = api.updateQuizScore(quizId, score)
            return response.data
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getQuizList(): List<Quiz> {
        val response = api.getQuizList()
        if (response.status == "success" && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message ?: "Unknown error")
        }
    }

    override suspend fun getQuizListForUser(userId: String): List<Quiz> {
        val response = api.getUserQuizList(userId)
        if (response.status == "success" && response.data != null) {
            return response.data.map { quizId ->
                getQuiz(quizId.id)
            }
        } else {
            throw Exception(response.message ?: "Unknown error")
        }
    }

}