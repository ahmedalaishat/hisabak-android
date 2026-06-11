package com.hisabak.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hisabak.ui.theme.HisabakTheme
import com.hisabak.ui.theme.HisabakType
import java.text.NumberFormat
import java.util.Locale

/*
 * Worked examples — these show how the HTML/React design-system primitives translate
 * to Composables against the Hisabak theme. They are a PATTERN to copy, not the full
 * component set. Match the originals' look (see the design system's component cards +
 * .prompt.md files) but use your app's existing Composables where they already exist.
 */

/**
 * AmountText — money with tabular Geist Mono figures and signed coloring.
 * Mirrors components/core/AmountText. Income green, expense coral.
 *
 *   AmountText(value = 8200.0)            // +SAR 8,200.00 (green)
 *   AmountText(value = -342.75)           // −SAR 342.75 (coral)
 *   AmountText(value = 12450.0, tone = AmountTone.Neutral, showSign = false, size = 40.sp)
 */
enum class AmountTone { Auto, Income, Expense, Savings, Investment, Neutral }

@Composable
fun AmountText(
    value: Double,
    modifier: Modifier = Modifier,
    currency: String = "SAR",
    showSign: Boolean = true,
    tone: AmountTone = AmountTone.Auto,
    size: androidx.compose.ui.unit.TextUnit = 16.sp,
    weight: FontWeight = FontWeight.SemiBold,
) {
    val c = HisabakTheme.colors
    val resolved = when (tone) {
        AmountTone.Auto -> if (value < 0) AmountTone.Expense else AmountTone.Income
        else -> tone
    }
    val color = when (resolved) {
        AmountTone.Income -> c.income
        AmountTone.Expense -> c.expense
        AmountTone.Savings -> c.savings
        AmountTone.Investment -> c.investment
        else -> androidx.compose.material3.MaterialTheme.colorScheme.onSurface
    }
    val nf = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2; maximumFractionDigits = 2
    }
    val prefix = if (showSign && tone != AmountTone.Neutral) (if (value < 0) "\u2212" else "+") else ""
    Text(
        text = "$prefix$currency ${nf.format(kotlin.math.abs(value))}",
        color = color,
        style = HisabakType.amount.copy(fontSize = size, fontWeight = weight),
        modifier = modifier,
    )
}

/**
 * StatusChip — SMS parse state. Mirrors components/core/StatusChip.
 * linked → green, parsed → blue (info), unparsed → gray.
 */
enum class SmsStatus { Linked, Parsed, Unparsed }

@Composable
fun StatusChip(status: SmsStatus, modifier: Modifier = Modifier) {
    val c = HisabakTheme.colors
    val (label, bg, fg) = when (status) {
        SmsStatus.Linked   -> Triple("Linked", c.incomeSoft, c.income)
        SmsStatus.Parsed   -> Triple("Parsed", c.infoSoft, c.info)
        SmsStatus.Unparsed -> Triple("Unparsed", c.surfaceSunken, c.textTertiary)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier
            .background(bg, RoundedCornerShape(percent = 50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(label, color = fg, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
    }
}
