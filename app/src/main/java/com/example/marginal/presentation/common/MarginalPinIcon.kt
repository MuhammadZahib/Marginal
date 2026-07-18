package com.example.marginal.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

/**
 * The Marginal "pin" mark — used on Splash, the FAB, and the save icon.
 * Drawn as a path directly instead of a drawable resource, so this file
 * works with zero setup (no res/drawable steps needed).
 */
@Composable
fun MarginalPinIcon(
    modifier: Modifier = Modifier,
    bodyColor: Color,
    dotColor: Color,
) {
    Canvas(modifier = modifier) {
        val scale = size.minDimension / 24f

        fun point(x: Float, y: Float) = Offset(x * scale, y * scale)

        val path = Path().apply {
            moveTo(point(12f, 2f).x, point(12f, 2f).y)
            cubicTo(
                point(8.7f, 2f).x, point(8.7f, 2f).y,
                point(6f, 4.7f).x, point(6f, 4.7f).y,
                point(6f, 8f).x, point(6f, 8f).y,
            )
            cubicTo(
                point(6f, 12.5f).x, point(6f, 12.5f).y,
                point(12f, 20f).x, point(12f, 20f).y,
                point(12f, 20f).x, point(12f, 20f).y,
            )
            cubicTo(
                point(12f, 20f).x, point(12f, 20f).y,
                point(18f, 12.5f).x, point(18f, 12.5f).y,
                point(18f, 8f).x, point(18f, 8f).y,
            )
            cubicTo(
                point(18f, 4.7f).x, point(18f, 4.7f).y,
                point(15.3f, 2f).x, point(15.3f, 2f).y,
                point(12f, 2f).x, point(12f, 2f).y,
            )
            close()
        }
        drawPath(path = path, color = bodyColor)
        drawCircle(color = dotColor, radius = 2.6f * scale, center = point(12f, 8f))
    }
}
