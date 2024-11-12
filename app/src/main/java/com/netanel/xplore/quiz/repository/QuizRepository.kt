package com.netanel.xplore.quiz.repository

import com.google.firebase.storage.FirebaseStorage
import com.netanel.xplore.quiz.model.Question
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class QuizRepository @Inject constructor(val storage: FirebaseStorage) {

    private val storageRef = storage.reference

    suspend fun getQuestions(): List<Question> {
        return try {
            // Access your JSON file from Firebase Storage root folder
            val jsonFileRef = storageRef.child("test_questions.json")

            // Use getBytes() to fetch data from Firebase Storage
            val maxSize = 1 * 1024 * 1024 // Maximum file size you expect, here 1MB
            val bytes = jsonFileRef.getBytes(maxSize.toLong()).await()

            // Convert bytes to a String
            val jsonString = bytes.decodeToString()

            // Deserialize JSON to List<Question>
            Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Return empty list if there's an error
        }
    }
}