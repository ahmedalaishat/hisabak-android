package com.hisabak.feature.dashboard.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.component.LineComponent

@Composable
fun AreaLineChart(
    values: List<Double>,
    lineColor: Color,
    fillColor: Color,
    modifier: Modifier = Modifier,
    heightDp: Dp = 120.dp,
) {
    val producer = remember { CartesianChartModelProducer() }
    LaunchedEffect(values) {
        if (values.isEmpty()) return@LaunchedEffect
        producer.runTransaction { lineSeries { series(values) } }
    }
    if (values.isEmpty()) return
    val line = LineCartesianLayer.rememberLine(
        fill = LineCartesianLayer.LineFill.single(fill(lineColor)),
        areaFill = LineCartesianLayer.AreaFill.single(fill(fillColor)),
    )
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(line),
            ),
        ),
        modelProducer = producer,
        modifier = modifier.height(heightDp),
    )
}

@Composable
fun BarSparkline(
    values: List<Double>,
    barColor: Color,
    modifier: Modifier = Modifier,
    heightDp: Dp = 64.dp,
) {
    val producer = remember { CartesianChartModelProducer() }
    LaunchedEffect(values) {
        if (values.isEmpty()) return@LaunchedEffect
        producer.runTransaction { columnSeries { series(values) } }
    }
    if (values.isEmpty()) return
    val column = remember(barColor) {
        LineComponent(fill = Fill(barColor.toArgb()), thicknessDp = 4f)
    }
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = ColumnCartesianLayer.ColumnProvider.series(column),
            ),
        ),
        modelProducer = producer,
        modifier = modifier.height(heightDp),
    )
}

/** Side-by-side income + expense bars per time bucket. */
@Composable
fun GroupedBarChart(
    incomeValues: List<Double>,
    expenseValues: List<Double>,
    incomeColor: Color,
    expenseColor: Color,
    modifier: Modifier = Modifier,
    heightDp: Dp = 140.dp,
) {
    if (incomeValues.isEmpty() || expenseValues.isEmpty()) return
    val producer = remember { CartesianChartModelProducer() }
    LaunchedEffect(incomeValues, expenseValues) {
        producer.runTransaction {
            columnSeries {
                series(incomeValues)
                series(expenseValues)
            }
        }
    }
    val incomeCol = remember(incomeColor) {
        LineComponent(fill = Fill(incomeColor.toArgb()), thicknessDp = 5f)
    }
    val expenseCol = remember(expenseColor) {
        LineComponent(fill = Fill(expenseColor.toArgb()), thicknessDp = 5f)
    }
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = ColumnCartesianLayer.ColumnProvider.series(incomeCol, expenseCol),
            ),
        ),
        modelProducer = producer,
        modifier = modifier.height(heightDp),
    )
}
