package com.quizapp.presentation.quiz

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quizapp.presentation.components.*
import com.quizapp.presentation.results.ResultsScreen

@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is QuizScreenState.Loading  -> QuizLoadingContent()
        is QuizScreenState.Error    -> QuizErrorContent(
            message = state.message,
            onRetry = { viewModel.onEvent(QuizEvent.RestartQuiz) }
        )
        is QuizScreenState.Active   -> QuizActiveContent(
            quizState = state.quizState,
            onEvent = viewModel::onEvent
        )
        is QuizScreenState.Finished -> ResultsScreen(
            result = state.result,
            onRestart = { viewModel.onEvent(QuizEvent.RestartQuiz) }
        )
    }
}

@Composable
private fun QuizActiveContent(
    quizState: QuizState,
    onEvent: (QuizEvent) -> Unit
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            QuizHeader()
            Spacer(modifier = Modifier.height(24.dp))
            QuizProgressHeader(
                currentQuestion = quizState.currentQuestionNumber,
                totalQuestions = quizState.totalQuestions,
                progress = quizState.progress
            )
            Spacer(modifier = Modifier.height(28.dp))
            AnimatedQuestionCard(
                quizState = quizState,
                onEvent = onEvent,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.height(20.dp))
            NavigationRow(quizState = quizState, onEvent = onEvent)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun QuizHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text = "📝",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(10.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "Grammar Quiz",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Test your English grammar knowledge",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AnimatedQuestionCard(
    quizState: QuizState,
    onEvent: (QuizEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var previousIndex by remember { mutableIntStateOf(quizState.currentIndex) }
    var slideDirection by remember { mutableIntStateOf(1) }

    LaunchedEffect(quizState.currentIndex) {
        slideDirection = if (quizState.currentIndex > previousIndex) 1 else -1
        previousIndex = quizState.currentIndex
    }

    AnimatedContent(
        targetState = quizState.currentIndex,
        transitionSpec = {
            val enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth * slideDirection },
                animationSpec = tween(300, easing = EaseOutCubic)
            ) + fadeIn(tween(300))

            val exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth * slideDirection },
                animationSpec = tween(300, easing = EaseInCubic)
            ) + fadeOut(tween(200))

            enter togetherWith exit
        },
        label = "question_transition",
        modifier = modifier
    ) { _ ->
        QuestionCard(quizState = quizState, onEvent = onEvent)
    }
}

@Composable
private fun QuestionCard(
    quizState: QuizState,
    onEvent: (QuizEvent) -> Unit
) {
    val question = quizState.currentQuestion
    val selectedOption = quizState.selectedOptionForCurrent

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    text = question.category,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = question.questionText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(20.dp))

            question.options.forEach { option ->
                QuizOptionItem(
                    option = option,
                    isSelected = selectedOption == option.key,
                    onSelect = { onEvent(QuizEvent.SelectOption(question.id, option.key)) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun NavigationRow(
    quizState: QuizState,
    onEvent: (QuizEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = { onEvent(QuizEvent.NavigatePrevious) },
            enabled = !quizState.isFirstQuestion,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.height(52.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Previous",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Previous")
        }

        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Text(
                text = "${quizState.answeredCount}/${quizState.totalQuestions} answered",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }

        if (quizState.isLastQuestion) {
            Button(
                onClick = { onEvent(QuizEvent.SubmitQuiz) },
                enabled = quizState.answers.size == quizState.totalQuestions,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Submit", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Text("✓")
            }
        } else {
            Button(
                onClick = { onEvent(QuizEvent.NavigateNext) },
                enabled = quizState.selectedOptionForCurrent != null,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.height(52.dp)
            ) {
                Text("Next")
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}