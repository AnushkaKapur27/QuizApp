package com.quizapp.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizapp.domain.model.Question
import com.quizapp.domain.model.QuestionReview
import com.quizapp.domain.model.QuizResult
import com.quizapp.domain.model.isAnswerCorrect
import com.quizapp.domain.usecase.GetQuestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuizScreenState>(QuizScreenState.Loading)
    val uiState: StateFlow<QuizScreenState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    fun onEvent(event: QuizEvent) {
        when (event) {
            is QuizEvent.SelectOption    -> handleSelectOption(event.questionId, event.optionKey)
            is QuizEvent.NavigateNext    -> handleNavigateNext()
            is QuizEvent.NavigatePrevious -> handleNavigatePrevious()
            is QuizEvent.SubmitQuiz      -> handleSubmitQuiz()
            is QuizEvent.RestartQuiz     -> loadQuestions()
        }
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            _uiState.value = QuizScreenState.Loading
            getQuestionsUseCase()
                .onSuccess { questions ->
                    _uiState.value = QuizScreenState.Active(
                        QuizState(
                            questions = questions,
                            currentIndex = 0,
                            answers = emptyMap()
                        )
                    )
                }
                .onFailure { error ->
                    _uiState.value = QuizScreenState.Error(
                        error.message ?: "Failed to load questions"
                    )
                }
        }
    }

    private fun handleSelectOption(questionId: Int, optionKey: String) {
        updateActiveState { state ->
            state.copy(answers = state.answers + (questionId to optionKey))
        }
    }

    private fun handleNavigateNext() {
        viewModelScope.launch {
            updateActiveState { it.copy(isTransitioning = true) }
            delay(50)
            updateActiveState { state ->
                if (!state.isLastQuestion) {
                    state.copy(currentIndex = state.currentIndex + 1, isTransitioning = false)
                } else {
                    state.copy(isTransitioning = false)
                }
            }
        }
    }

    private fun handleNavigatePrevious() {
        viewModelScope.launch {
            updateActiveState { it.copy(isTransitioning = true) }
            delay(50)
            updateActiveState { state ->
                if (!state.isFirstQuestion) {
                    state.copy(currentIndex = state.currentIndex - 1, isTransitioning = false)
                } else {
                    state.copy(isTransitioning = false)
                }
            }
        }
    }

    private fun handleSubmitQuiz() {
        val currentState = _uiState.value as? QuizScreenState.Active ?: return
        val quizState = currentState.quizState
        val result = buildQuizResult(quizState.questions, quizState.answers)
        _uiState.value = QuizScreenState.Finished(result)
    }

    private fun buildQuizResult(
        questions: List<Question>,
        answers: Map<Int, String>
    ): QuizResult {
        val reviews = questions.mapIndexed { index, question ->
            val selected = answers[question.id]
            val isCorrect = selected != null && question.isAnswerCorrect(selected)
            QuestionReview(
                questionNumber = index + 1,
                question = question,
                userSelectedOption = selected,
                isCorrect = isCorrect
            )
        }
        return QuizResult(
            totalQuestions = questions.size,
            correctAnswers = reviews.count { it.isCorrect },
            incorrectAnswers = reviews.count { !it.isCorrect },
            questionReviews = reviews
        )
    }

    private inline fun updateActiveState(transform: (QuizState) -> QuizState) {
        val current = _uiState.value as? QuizScreenState.Active ?: return
        _uiState.update { QuizScreenState.Active(transform(current.quizState)) }
    }
}