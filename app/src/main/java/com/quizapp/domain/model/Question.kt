package com.quizapp.domain.model

data class Question(
    val id: Int,
    val questionText: String,
    val options: List<QuizOption>,
    val correctOption: String,
    val explanation: String,
    val category: String
)

data class QuizOption(
    val key: String,
    val text: String
)

data class UserAnswer(
    val questionId: Int,
    val selectedOption: String
)

fun Question.isAnswerCorrect(selectedOption: String): Boolean {
    return selectedOption == correctOption
}

fun Question.getCorrectAnswerText(): String {
    return options.find { it.key == correctOption }?.text ?: ""
}

fun Question.getSelectedAnswerText(selectedOption: String): String {
    return options.find { it.key == selectedOption }?.text ?: ""
}