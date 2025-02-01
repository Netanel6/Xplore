package com.netanel.xplore.localDatabase.user.converters

import com.netanel.xplore.auth.repository.model.User
import com.netanel.xplore.localDatabase.user.model.QuizEntity
import com.netanel.xplore.localDatabase.user.model.UserEntity

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id ?: "",
        name = this.name,
        phoneNumber = this.phoneNumber,
        token = this.token,
        quizzes = this.quizzes.map { QuizEntity(it.id, it.title) }
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        phoneNumber = this.phoneNumber,
        token = this.token,
        quizzes = this.quizzes.map { User.Quiz(it.id, it.title) }
    )
}


