package com.quizapp.di

import android.content.Context
import com.quizapp.data.local.dao.QuestionDao
import com.quizapp.data.local.database.DatabaseSeeder
import com.quizapp.data.local.database.QuizDatabase
import com.quizapp.data.repository.QuestionRepository
import com.quizapp.data.repository.QuestionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideQuizDatabase(@ApplicationContext context: Context): QuizDatabase {
        return QuizDatabase.buildDatabase(context)
    }

    @Provides
    @Singleton
    fun provideQuestionDao(db: QuizDatabase): QuestionDao {
        return db.questionDao()
    }

    @Provides
    @Singleton
    fun provideDatabaseSeeder(questionDao: QuestionDao): DatabaseSeeder {
        return DatabaseSeeder(questionDao)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindQuestionRepository(
        impl: QuestionRepositoryImpl
    ): QuestionRepository
}