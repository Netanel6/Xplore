package com.netanel.xplore.localDatabase.user.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.netanel.xplore.localDatabase.user.model.QuizEntity

class QuizListConverter {
    @TypeConverter
    fun fromQuizList(value: List<QuizEntity>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toQuizList(value: String): List<QuizEntity> {
        val listType = object : TypeToken<List<QuizEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
