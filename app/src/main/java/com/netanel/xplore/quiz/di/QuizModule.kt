package com.netanel.xplore.quiz.di
import com.netanel.xplore.quiz.data.QuizApi
import com.netanel.xplore.quiz.repository.QuizRepository
import com.netanel.xplore.quiz.repository.QuizRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QuizModule {

    @Provides
    @Singleton
    fun provideQuestionsApi(retrofit: Retrofit): QuizApi {
        return retrofit.create(QuizApi::class.java)
    }

    @Provides
    @Singleton
    fun provideQuestionsRepository(api: QuizApi): QuizRepository {
        return QuizRepositoryImpl(api)
    }
}


