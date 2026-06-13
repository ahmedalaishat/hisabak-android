package com.hisabak.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hisabak.R
import com.hisabak.ui.theme.HisabakTheme
import com.hisabak.ui.theme.HisabakType
import com.hisabak.ui.theme.PillShape
import com.hisabak.ui.theme.Spacing
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

/*
 * Worked examples — these show how the HTML/React design-system primitives translate
 * to Composables against the Hisabak theme. They are a PATTERN to copy, not the full
 * component set. Match the originals' look (see the design system's component cards +
 * .prompt.md files) but use your app's existing Composables where they already exist.
 */

/**
 * DirhamGlyph — the AED currency mark (res/drawable/ic_dirham), sized to sit
 * inline with text and tinted to match. Use before a number instead of "AED".
 */
private const val DIRHAM_ASPECT = 1000f / 870f // symbol is wider than tall

@Composable
fun DirhamGlyph(
    modifier: Modifier = Modifier,
    size: TextUnit = 14.sp,
    tint: Color = LocalContentColor.current,
) {
    val heightDp = with(LocalDensity.current) { size.toDp() }
    Icon(
        painter = painterResource(R.drawable.ic_dirham),
        contentDescription = "AED",
        tint = tint,
        modifier = modifier
            .height(heightDp)
            .width(heightDp * DIRHAM_ASPECT),
    )
}

/**
 * AmountText — money with tabular Geist Mono figures and signed coloring.
 * Renders the dirham glyph in place of a currency code. Income green, expense coral.
 *
 *   AmountText(value = 8200.0)            // +⊅ 8,200.00 (green)
 *   AmountText(value = -342.75)           // −⊅ 342.75 (coral)
 *   AmountText(value = 12450.0, tone = AmountTone.Neutral, showSign = false, size = 40.sp)
 */
enum class AmountTone { Auto, Income, Expense, Savings, Investment, Neutral }

@Composable
fun AmountText(
    value: Double,
    modifier: Modifier = Modifier,
    currency: String = "AED", // retained for API compatibility; AED renders as the dirham glyph
    showSign: Boolean = true,
    tone: AmountTone = AmountTone.Auto,
    size: TextUnit = 16.sp,
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
        else -> MaterialTheme.colorScheme.onSurface
    }
    val nf = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2; maximumFractionDigits = 2
    }
    val sign = if (showSign && tone != AmountTone.Neutral) (if (value < 0) "−" else "+") else ""
    val numberStyle = HisabakType.amount.copy(fontSize = size, fontWeight = weight)
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        if (sign.isNotEmpty()) Text(sign, color = color, style = numberStyle)
        DirhamGlyph(size = size * 0.82f, tint = color)
        Spacer(Modifier.width(3.dp))
        Text(nf.format(abs(value)), color = color, style = numberStyle)
    }
}

/**
 * MoneyText — dirham glyph + grouped amount (no decimals, "M" for millions),
 * for headline/summary figures that take a [Money]'s minor units.
 */
@Composable
fun MoneyText(
    amountMinor: Long,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
    symbolScale: Float = 0.8f,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        DirhamGlyph(size = style.fontSize * symbolScale, tint = color)
        Spacer(Modifier.width(3.dp))
        Text(formatGroupedMajor(amountMinor), style = style, color = color, maxLines = 1)
    }
}

private fun formatGroupedMajor(amountMinor: Long): String {
    val major = amountMinor / 100.0
    return if (abs(major) >= 1_000_000) "%.2fM".format(major / 1_000_000.0)
    else "%,.0f".format(major)
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
            .background(bg, PillShape)
            .padding(horizontal = 10.dp, vertical = Spacing.s2),
    ) {
        Text(label, color = fg, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
    }
}
