package com.quizapp.domain.usecase

import com.quizapp.data.repository.QuestionRepository
import com.quizapp.domain.model.Question
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val repository: QuestionRepository
) {
    suspend operator fun invoke(): Result<List<Question>> {
        return runCatching { repository.getQuestions() }
    }
}