package com.netanel.xplore.localDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.netanel.xplore.localDatabase.user.converters.QuizListConverter
import com.netanel.xplore.localDatabase.user.dao.UserDao
import com.netanel.xplore.localDatabase.user.model.QuizEntity
import com.netanel.xplore.localDatabase.user.model.UserEntity

@Database(entities = [UserEntity::class, QuizEntity::class], version = 1, exportSchema = false)
@TypeConverters(QuizListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
