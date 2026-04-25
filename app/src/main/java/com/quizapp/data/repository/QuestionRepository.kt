package com.quizapp.data.repository

import com.quizapp.domain.model.Question

interface QuestionRepository {
    suspend fun getQuestions(): List<Question>
}