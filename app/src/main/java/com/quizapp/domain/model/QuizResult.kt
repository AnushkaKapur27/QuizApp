package com.quizapp.domain.model

data class QuizResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val questionReviews: List<QuestionReview>
) {
    val scorePercentage: Float
        get() = if (totalQuestions > 0) correctAnswers.toFloat() / totalQuestions else 0f

    val isPerfectScore: Boolean
        get() = correctAnswers == totalQuestions

    val grade: String
        get() = when {
            scorePercentage >= 0.9f -> "A"
            scorePercentage >= 0.8f -> "B"
            scorePercentage >= 0.7f -> "C"
            scorePercentage >= 0.6f -> "D"
            else -> "F"
        }

    val gradeMessage: String
        get() = when {
            scorePercentage >= 0.9f -> "Outstanding! 🎯"
            scorePercentage >= 0.7f -> "Great Job! 🌟"
            scorePercentage >= 0.5f -> "Good Effort! 💪"
            else -> "Keep Practicing! 📚"
        }
}

data class QuestionReview(
    val questionNumber: Int,
    val question: Question,
    val userSelectedOption: String?,
    val isCorrect: Boolean
)