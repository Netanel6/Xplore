package com.netanel.xplore.temp.di
import android.content.Context
import com.netanel.xplore.temp.repository.QuizRepository
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
        @ApplicationContext context: Context
    ): QuizRepository {
        return QuizRepository(context)
    }
}