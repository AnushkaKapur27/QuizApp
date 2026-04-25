package com.quizapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Indigo50  = Color(0xFFEEF2FF)
val Indigo100 = Color(0xFFE0E7FF)
val Indigo200 = Color(0xFFC7D2FE)
val Indigo400 = Color(0xFF818CF8)
val Indigo500 = Color(0xFF6366F1)
val Indigo600 = Color(0xFF4F46E5)
val Indigo700 = Color(0xFF4338CA)
val Indigo900 = Color(0xFF312E81)

val Emerald400 = Color(0xFF34D399)
val Emerald500 = Color(0xFF10B981)
val Emerald600 = Color(0xFF059669)

val Rose400 = Color(0xFFFB7185)
val Rose500 = Color(0xFFF43F5E)
val Rose600 = Color(0xFFE11D48)

val Amber400 = Color(0xFFFBBF24)
val Amber500 = Color(0xFFF59E0B)

val Slate50  = Color(0xFFF8FAFC)
val Slate100 = Color(0xFFF1F5F9)
val Slate200 = Color(0xFFE2E8F0)
val Slate700 = Color(0xFF334155)
val Slate800 = Color(0xFF1E293B)
val Slate900 = Color(0xFF0F172A)

val CorrectGreen  = Emerald500
val IncorrectRed  = Rose500
val SelectedIndigo = Indigo500
val UnselectedBg  = Color(0xFFF8FAFC)

private val LightColorScheme = lightColorScheme(
    primary             = Indigo600,
    onPrimary           = Color.White,
    primaryContainer    = Indigo100,
    onPrimaryContainer  = Indigo900,
    secondary           = Emerald500,
    onSecondary         = Color.White,
    secondaryContainer  = Color(0xFFD1FAE5),
    onSecondaryContainer = Color(0xFF065F46),
    error               = Rose500,
    onError             = Color.White,
    errorContainer      = Color(0xFFFFE4E6),
    onErrorContainer    = Color(0xFF9F1239),
    background          = Slate50,
    onBackground        = Slate900,
    surface             = Color.White,
    onSurface           = Slate800,
    surfaceVariant      = Slate100,
    onSurfaceVariant    = Slate700,
    outline             = Slate200
)

private val DarkColorScheme = darkColorScheme(
    primary             = Indigo400,
    onPrimary           = Indigo900,
    primaryContainer    = Indigo700,
    onPrimaryContainer  = Indigo100,
    secondary           = Emerald400,
    onSecondary         = Color(0xFF065F46),
    secondaryContainer  = Emerald600,
    onSecondaryContainer = Color(0xFFD1FAE5),
    error               = Rose400,
    onError             = Color(0xFF9F1239),
    background          = Slate900,
    onBackground        = Slate50,
    surface             = Slate800,
    onSurface           = Slate100,
    surfaceVariant      = Slate700,
    onSurfaceVariant    = Slate200,
    outline             = Slate700
)

@Composable
fun QuizAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = QuizTypography,
        content = content
    )
}