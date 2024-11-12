package com.netanel.xplore.quiz.di
import android.content.Context
import com.google.firebase.storage.FirebaseStorage
import com.netanel.xplore.quiz.repository.QuizRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideQuizRepository(
        firebaseStorage: FirebaseStorage
    ): QuizRepository {
        return QuizRepository(firebaseStorage)
    }
}