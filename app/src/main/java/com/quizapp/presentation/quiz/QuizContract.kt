package com.quizapp.presentation.quiz

import com.quizapp.domain.model.Question
import com.quizapp.domain.model.QuizResult

sealed interface QuizScreenState {
    data object Loading : QuizScreenState
    data class Active(val quizState: QuizState) : QuizScreenState
    data class Finished(val result: QuizResult) : QuizScreenState
    data class Error(val message: String) : QuizScreenState
}

data class QuizState(
    val questions: List<Question>,
    val currentIndex: Int,
    val answers: Map<Int, String>,
    val isTransitioning: Boolean = false
) {
    val currentQuestion: Question get() = questions[currentIndex]
    val totalQuestions: Int get() = questions.size
    val currentQuestionNumber: Int get() = currentIndex + 1
    val progress: Float get() = (currentIndex + 1).toFloat() / totalQuestions
    val selectedOptionForCurrent: String? get() = answers[currentQuestion.id]
    val isLastQuestion: Boolean get() = currentIndex == totalQuestions - 1
    val isFirstQuestion: Boolean get() = currentIndex == 0
    val answeredCount: Int get() = answers.size
}

sealed interface QuizEvent {
    data class SelectOption(val questionId: Int, val optionKey: String) : QuizEvent
    data object NavigateNext : QuizEvent
    data object NavigatePrevious : QuizEvent
    data object SubmitQuiz : QuizEvent
    data object RestartQuiz : QuizEvent
}