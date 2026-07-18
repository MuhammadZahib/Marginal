package com.example.marginal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MarginalColorScheme = lightColorScheme(
    primary = Ink,
    background = Paper,
    onBackground = TextPrimary,
    surface = PaperCard,
    onSurface = TextPrimary,
    secondary = Amber,
    error = Brick,
)

@Composable
fun MarginalTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MarginalColorScheme,
        typography = MarginalTypography,
        content = content,
    )
}