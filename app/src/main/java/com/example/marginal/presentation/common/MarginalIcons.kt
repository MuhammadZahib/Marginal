package com.example.marginal.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/** Tappable circular wrapper for a single icon — gives a consistent 40dp touch target. */
@Composable
fun MarginalIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

/** Icon + "Back" label together — a bare icon alone reads as ambiguous, this is the one to use for navigation. */
@Composable
fun MarginalBackButton(onClick: () -> Unit, tint: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BackArrowIcon(modifier = Modifier.size(18.dp), tint = tint)
        Spacer(modifier = Modifier.width(4.dp))
        Text("Back", color = tint, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun BackArrowIcon(modifier: Modifier = Modifier, tint: Color) {
    Canvas(modifier = modifier) {
        val scale = size.minDimension / 24f
        fun pt(x: Float, y: Float) = Offset(x * scale, y * scale)
        val path = Path().apply {
            moveTo(pt(15f, 5f).x, pt(15f, 5f).y)
            lineTo(pt(8f, 12f).x, pt(8f, 12f).y)
            lineTo(pt(15f, 19f).x, pt(15f, 19f).y)
        }
        drawPath(path, color = tint, style = Stroke(width = 2f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun CloseIcon(modifier: Modifier = Modifier, tint: Color) {
    Canvas(modifier = modifier) {
        val scale = size.minDimension / 24f
        fun pt(x: Float, y: Float) = Offset(x * scale, y * scale)
        val strokeWidth = 2f * scale
        drawLine(tint, pt(18f, 6f), pt(6f, 18f), strokeWidth, cap = StrokeCap.Round)
        drawLine(tint, pt(6f, 6f), pt(18f, 18f), strokeWidth, cap = StrokeCap.Round)
    }
}

@Composable
fun CheckIcon(modifier: Modifier = Modifier, tint: Color) {
    Canvas(modifier = modifier) {
        val scale = size.minDimension / 24f
        fun pt(x: Float, y: Float) = Offset(x * scale, y * scale)
        val path = Path().apply {
            moveTo(pt(5f, 13f).x, pt(5f, 13f).y)
            lineTo(pt(9f, 17f).x, pt(9f, 17f).y)
            lineTo(pt(19f, 7f).x, pt(19f, 7f).y)
        }
        drawPath(path, color = tint, style = Stroke(width = 2.4f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun TrashIcon(modifier: Modifier = Modifier, tint: Color) {
    Canvas(modifier = modifier) {
        val scale = size.minDimension / 24f
        fun pt(x: Float, y: Float) = Offset(x * scale, y * scale)
        val strokeWidth = 1.8f * scale
        // lid line
        drawLine(tint, pt(3f, 6f), pt(21f, 6f), strokeWidth, cap = StrokeCap.Round)
        // handle
        val handle = Path().apply {
            moveTo(pt(8f, 6f).x, pt(8f, 6f).y)
            lineTo(pt(8f, 4f).x, pt(8f, 4f).y)
            lineTo(pt(16f, 4f).x, pt(16f, 4f).y)
            lineTo(pt(16f, 6f).x, pt(16f, 6f).y)
        }
        drawPath(handle, color = tint, style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round))
        // bin body
        val body = Path().apply {
            moveTo(pt(6f, 6f).x, pt(6f, 6f).y)
            lineTo(pt(7f, 21f).x, pt(7f, 21f).y)
            lineTo(pt(17f, 21f).x, pt(17f, 21f).y)
            lineTo(pt(18f, 6f).x, pt(18f, 6f).y)
        }
        drawPath(body, color = tint, style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun CameraIcon(modifier: Modifier = Modifier, tint: Color) {
    Canvas(modifier = modifier) {
        val scale = size.minDimension / 24f
        val strokeWidth = 1.7f * scale
        drawRoundRect(
            color = tint,
            topLeft = Offset(3f * scale, 7f * scale),
            size = androidx.compose.ui.geometry.Size(18f * scale, 13f * scale),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.5f * scale, 2.5f * scale),
            style = Stroke(width = strokeWidth),
        )
        drawCircle(
            color = tint,
            radius = 3.5f * scale,
            center = Offset(12f * scale, 13.5f * scale),
            style = Stroke(width = strokeWidth),
        )
        val notch = Path().apply {
            moveTo(8f * scale, 7f * scale)
            lineTo(9.2f * scale, 4.8f * scale)
            lineTo(14.8f * scale, 4.8f * scale)
            lineTo(16f * scale, 7f * scale)
        }
        drawPath(notch, color = tint, style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}
