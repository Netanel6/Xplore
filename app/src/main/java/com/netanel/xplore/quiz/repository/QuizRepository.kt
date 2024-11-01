package com.netanel.xplore.quiz.repository

import android.content.Context
import com.netanel.xplore.quiz.model.Question
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class QuizRepository(private val context: Context) {

    fun getQuestions(): List<Question> {
        //questions.json
        val jsonString = context.assets.open("test_questions.json").bufferedReader().use { it.readText() }
        return Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)
    }
}