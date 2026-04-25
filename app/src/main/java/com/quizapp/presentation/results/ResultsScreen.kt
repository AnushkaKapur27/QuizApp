package com.quizapp.presentation.results

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.quizapp.domain.model.*
import com.quizapp.presentation.theme.*

@Composable
fun ResultsScreen(
    result: QuizResult,
    onRestart: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item { ResultsHeroSection(result = result, onRestart = onRestart) }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            ScoreStatsRow(result = result)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "Review Answers",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        itemsIndexed(
            items = result.questionReviews,
            key = { _, review -> review.questionNumber }
        ) { index, review ->
            ReviewItem(
                review = review,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    //.animateItem(fadeInSpec = tween(300, delayMillis = index * 60))
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ResultsHeroSection(result: QuizResult, onRestart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = 32.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieOrFallbackAnimation(isPerfect = result.isPerfectScore)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = result.gradeMessage,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            ScoreRing(
                score = result.correctAnswers,
                total = result.totalQuestions,
                percentage = result.scorePercentage
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                onClick = onRestart,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp, Color.White.copy(alpha = 0.6f)
                ),
                modifier = Modifier.height(48.dp)
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Restart",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Try Again", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun LottieOrFallbackAnimation(isPerfect: Boolean) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(
            if (isPerfect) "lottie_trophy.json" else "lottie_stars.json"
        )
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    if (composition != null) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(120.dp)
        )
    } else {
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.12f,
            animationSpec = infiniteRepeatable(
                animation = tween(700, easing = EaseInOut),
                repeatMode = RepeatMode.Reverse
            ),
            label = "emojiPulse"
        )
        Text(
            text = if (isPerfect) "🏆" else "⭐",
            fontSize = (72 * scale).sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.size(100.dp)
        )
    }
}

@Composable
private fun ScoreRing(score: Int, total: Int, percentage: Float) {
    val animatedSweep by animateFloatAsState(
        targetValue = percentage * 360f,
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "sweepAngle"
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
        androidx.compose.foundation.Canvas(modifier = Modifier.size(130.dp)) {
            drawArc(
                color = Color.White.copy(alpha = 0.2f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = Color.White,
                startAngle = -90f,
                sweepAngle = animatedSweep,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$score/$total",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "correct",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.75f)
            )
        }
    }
}

@Composable
private fun ScoreStatsRow(result: QuizResult) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard("✅", "Correct",   result.correctAnswers.toString(),   CorrectGreen,   Modifier.weight(1f))
        StatCard("❌", "Incorrect", result.incorrectAnswers.toString(), IncorrectRed,   Modifier.weight(1f))
        StatCard("🎓", "Grade",     result.grade,                       SelectedIndigo, Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(
    emoji: String,
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ReviewItem(review: QuestionReview, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300),
        label = "chevron"
    )

    val correctAnswerText  = review.question.getCorrectAnswerText()
    val selectedAnswerText = review.userSelectedOption
        ?.let { review.question.getSelectedAnswerText(it) } ?: "Not answered"

    val indicatorColor = if (review.isCorrect) CorrectGreen else IncorrectRed
    val indicatorBg    = indicatorColor.copy(alpha = 0.1f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { isExpanded = !isExpanded }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // ── Header ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = indicatorBg,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Q${review.questionNumber}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = indicatorColor
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (review.isCorrect) "Correct ✓" else "Incorrect ✗",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = indicatorColor
                    )
                    Text(
                        text = review.question.questionText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotationAngle)
                )
            }

            // ── Expanded body ────────────────────────────────────────────────
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(tween(300)) + fadeIn(tween(300)),
                exit  = shrinkVertically(tween(250)) + fadeOut(tween(200))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        modifier = Modifier.padding(bottom = 14.dp)
                    )

                    DetailRow(
                        label = "Question",
                        value = review.question.questionText,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        valueColor = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DetailRow(
                        label = "✅ Correct Answer",
                        value = "${review.question.correctOption}. $correctAnswerText",
                        labelColor = CorrectGreen,
                        valueColor = CorrectGreen
                    )

                    if (!review.isCorrect && review.userSelectedOption != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        DetailRow(
                            label = "❌ Your Answer",
                            value = "${review.userSelectedOption}. $selectedAnswerText",
                            labelColor = IncorrectRed,
                            valueColor = IncorrectRed
                        )
                    }

                    if (review.userSelectedOption == null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        DetailRow(
                            label = "⚠️ Your Answer",
                            value = "Not answered",
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            valueColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Text(text = "💡", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = review.question.explanation,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    labelColor: Color,
    valueColor: Color
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = labelColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = valueColor,
            lineHeight = 18.sp
        )
    }
}