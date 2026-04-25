package com.quizapp.data.repository

import com.quizapp.data.local.dao.QuestionDao
import com.quizapp.data.local.database.DatabaseSeeder
import com.quizapp.data.local.entity.QuestionEntity
import com.quizapp.domain.model.Question
import com.quizapp.domain.model.QuizOption
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val questionDao: QuestionDao,
    private val databaseSeeder: DatabaseSeeder
) : QuestionRepository {

    override suspend fun getQuestions(): List<Question> {
        databaseSeeder.seedIfEmpty()
        return questionDao.getQuestions(limit = 5).map { it.toDomainModel() }
    }

    private fun QuestionEntity.toDomainModel(): Question {
        return Question(
            id = id,
            questionText = questionText,
            options = listOf(
                QuizOption("A", optionA),
                QuizOption("B", optionB),
                QuizOption("C", optionC),
                QuizOption("D", optionD)
            ),
            correctOption = correctOption,
            explanation = explanation,
            category = category
        )
    }
}