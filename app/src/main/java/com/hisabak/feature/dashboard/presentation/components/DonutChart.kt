package com.hisabak.feature.dashboard.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

data class DonutSlice(val value: Double, val color: Color)

@Composable
fun DonutChart(
    slices: List<DonutSlice>,
    modifier: Modifier = Modifier,
    strokeWidthDp: Int = 14,
) {
    Canvas(modifier = modifier.size(80.dp)) {
        val stroke = strokeWidthDp.dp.toPx()
        val diameter = size.minDimension - stroke
        val offset = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
        val arcSize = Size(diameter, diameter)
        val total = slices.sumOf { it.value }
        if (total <= 0.0) {
            drawArc(
                color = Color.LightGray.copy(alpha = 0.3f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = offset,
                size = arcSize,
                style = Stroke(width = stroke),
            )
            return@Canvas
        }
        var start = -90f
        slices.forEach { slice ->
            val sweep = (slice.value / total * 360.0).toFloat()
            drawArc(
                color = slice.color,
                startAngle = start,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = offset,
                size = arcSize,
                style = Stroke(width = stroke),
            )
            start += sweep
        }
    }
}
