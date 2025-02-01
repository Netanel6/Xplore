package com.netanel.xplore.localDatabase.user.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.netanel.xplore.localDatabase.user.converters.QuizListConverter

@Entity(tableName = "users")
@TypeConverters(QuizListConverter::class)
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phoneNumber: String,
    val token: String,
    val quizzes: List<QuizEntity>
)
