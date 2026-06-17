package com.hisabak.feature.onboarding.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hisabak.ui.theme.HisabakTheme
import com.hisabak.ui.theme.HisabakType
import com.hisabak.ui.theme.LocalReducedMotion
import com.hisabak.ui.theme.Motion
import com.hisabak.ui.theme.Spacing
import com.hisabak.ui.theme.TileShape
import com.hisabak.ui.theme.motionDuration

/* ----------------------------- shared scaffold ----------------------------- */

@Composable
private fun OnboardingPage(
    active: Boolean,
    parallax: Float,
    overline: String,
    title: String,
    subtitle: String,
    hero: @Composable () -> Unit,
) {
    val reduced = LocalReducedMotion.current
    val enter by animateFloatAsState(
        targetValue = if (active) 1f else 0f,
        animationSpec = tween(motionDuration(420), easing = Motion.Easing.Standard),
        label = "enter",
    )
    val density = LocalDensity.current
    val shiftPx = with(density) { 36.dp.toPx() }
    val risePx = with(density) { 16.dp.toPx() }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = Spacing.s7)) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .graphicsLayer {
                    if (!reduced) translationX = parallax * shiftPx
                    alpha = enter
                },
            contentAlignment = Alignment.Center,
        ) { hero() }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.s3)
                .graphicsLayer {
                    alpha = enter
                    if (!reduced) translationY = (1f - enter) * risePx
                },
        ) {
            Text(
                text = overline.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(Spacing.s4))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(Spacing.s4))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun progressFor(active: Boolean): Float {
    val p by animateFloatAsState(
        targetValue = if (active) 1f else 0f,
        animationSpec = tween(motionDuration(900), easing = Motion.Easing.Standard),
        label = "viz",
    )
    return p
}

private fun money(v: Double): String = "AED %,.2f".format(v)

@Composable
private fun PreviewCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Spacing.s6))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(Spacing.s6))
            .padding(Spacing.s5),
    ) { content() }
}

@Composable
private fun CatTile(icon: ImageVector, color: Color) {
    Box(
        Modifier.size(44.dp).clip(TileShape).background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center,
    ) { Icon(icon, null, tint = color, modifier = Modifier.size(22.dp)) }
}

/* --------------------------------- page 1 ---------------------------------- */

@Composable
fun WelcomePage(active: Boolean, parallax: Float) {
    val p = progressFor(active)
    OnboardingPage(
        active, parallax,
        overline = "Welcome to Hisabak",
        title = "All your money, in one calm place.",
        subtitle = "Track spending, set budgets, and see where every dirham goes — without the busywork.",
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier.size(84.dp).clip(RoundedCornerShape(22.dp)).background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.TrendingUp, null,
                    tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(40.dp),
                )
            }
            Spacer(Modifier.height(Spacing.s7))
            PreviewCard(Modifier.width(300.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "NET WORTH",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(Spacing.s3))
                    Text(money(12480.0 * p), style = HisabakType.amountHero, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(Modifier.height(Spacing.s4))
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.s2),
                        modifier = Modifier.height(34.dp),
                    ) {
                        val accent = MaterialTheme.colorScheme.primary
                        val soft = HisabakTheme.colors.incomeSoft
                        listOf(14 to false, 20 to false, 13 to false, 26 to true, 18 to false, 30 to true, 34 to true)
                            .forEach { (h, hi) ->
                                Box(
                                    Modifier.width(9.dp).height((h * p).dp).clip(RoundedCornerShape(4.dp))
                                        .background(if (hi) accent else soft),
                                )
                            }
                    }
                }
            }
        }
    }
}

/* --------------------------------- page 2 ---------------------------------- */

@Composable
fun SmsCapturePage(active: Boolean, parallax: Float) {
    val p = progressFor(active)
    val rowReveal = ((p - 0.45f) / 0.55f).coerceIn(0f, 1f)
    val density = LocalDensity.current
    OnboardingPage(
        active, parallax,
        overline = "SMS auto-capture",
        title = "Your bank texts become transactions.",
        subtitle = "Hisabak reads the alert, pulls out the amount and merchant, and files it — automatically.",
    ) {
        Column(Modifier.width(320.dp), horizontalAlignment = Alignment.Start) {
            // SMS bubble
            Box(
                Modifier
                    .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = Spacing.s5, vertical = Spacing.s4),
            ) {
                Text(
                    "Purchase of AED 89.00 with card 1234 at Lulu, Abu Dhabi.",
                    style = HisabakType.amount.copy(fontWeight = FontWeight.Normal, fontSize = MaterialTheme.typography.bodySmall.fontSize),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Spacer(Modifier.height(Spacing.s4))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.s2)) {
                Icon(
                    Icons.Filled.ArrowDownward, null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp),
                )
                Text(
                    "parsed automatically",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.height(Spacing.s4))
            PreviewCard(
                Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = rowReveal
                        translationY = (1f - rowReveal) * with(density) { 16.dp.toPx() }
                    },
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CatTile(Icons.Filled.ShoppingCart, HisabakTheme.colors.catOrange)
                    Spacer(Modifier.width(Spacing.s4))
                    Column(Modifier.weight(1f)) {
                        Text("Lulu", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                        Text("Groceries · SMS", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("−AED 89.00", style = HisabakType.amount, color = HisabakTheme.colors.expense)
                }
            }
        }
    }
}

/* --------------------------------- page 3 ---------------------------------- */

@Composable
fun BudgetsPage(active: Boolean, parallax: Float) {
    val p = progressFor(active)
    val barFraction = 0.8f * p
    val warnT = ((p - 0.55f) / 0.45f).coerceIn(0f, 1f)
    val barColor = lerp(MaterialTheme.colorScheme.primary, HisabakTheme.colors.warning, warnT)
    OnboardingPage(
        active, parallax,
        overline = "Budgets & alerts",
        title = "Know before you overspend.",
        subtitle = "Set a monthly limit per category. We nudge you at 50%, 80%, and 100% — in-app and on your phone.",
    ) {
        PreviewCard(Modifier.width(320.dp)) {
            Column(Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CatTile(Icons.Filled.ShoppingCart, HisabakTheme.colors.catOrange)
                    Spacer(Modifier.width(Spacing.s4))
                    Column(Modifier.weight(1f)) {
                        Text("Groceries", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                        Text("June budget", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("AED 960 / 1,200", style = HisabakType.amount, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(Spacing.s4))
                Box(
                    Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(999.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Box(Modifier.fillMaxHeight().fillMaxWidth(barFraction).clip(RoundedCornerShape(999.dp)).background(barColor))
                }
                Spacer(Modifier.height(Spacing.s4))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.s2),
                        modifier = Modifier
                            .graphicsLayer { alpha = warnT }
                            .clip(RoundedCornerShape(999.dp))
                            .background(HisabakTheme.colors.warningSoft)
                            .padding(horizontal = Spacing.s4, vertical = Spacing.s2),
                    ) {
                        Icon(Icons.Filled.NotificationsActive, null, tint = HisabakTheme.colors.warning, modifier = Modifier.size(16.dp))
                        Text("80% of budget", style = MaterialTheme.typography.labelMedium, color = HisabakTheme.colors.warning)
                    }
                    Spacer(Modifier.weight(1f))
                    Text("AED 240 left", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

/* --------------------------------- page 4 ---------------------------------- */

@Composable
fun InsightsPage(active: Boolean, parallax: Float) {
    val p = progressFor(active)
    val c = HisabakTheme.colors
    val track = MaterialTheme.colorScheme.surfaceVariant
    val accent = MaterialTheme.colorScheme.primary
    OnboardingPage(
        active, parallax,
        overline = "Insights",
        title = "See exactly where it goes.",
        subtitle = "Net-worth trends, income vs spending, and a clean breakdown by category and brand.",
    ) {
        Column(Modifier.width(320.dp), verticalArrangement = Arrangement.spacedBy(Spacing.s4)) {
            PreviewCard(Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(Modifier.size(110.dp)) {
                        val stroke = 13f
                        val r = (size.minDimension - stroke) / 2f
                        val tl = Offset((size.width - 2 * r) / 2f, (size.height - 2 * r) / 2f)
                        val arcSize = androidx.compose.ui.geometry.Size(2 * r, 2 * r)
                        drawArc(track, 0f, 360f, false, tl, arcSize, style = Stroke(stroke))
                        var start = -90f
                        listOf(c.catOrange to 0.38f, c.catBlue to 0.26f, c.catPurple to 0.18f).forEach { (col, frac) ->
                            val sweep = 360f * frac * p
                            drawArc(col, start, sweep, false, tl, arcSize, style = Stroke(stroke, cap = StrokeCap.Round))
                            start += 360f * frac * p
                        }
                    }
                    Spacer(Modifier.width(Spacing.s5))
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.s3)) {
                        LegendRow(c.catOrange, "Groceries", "38%")
                        LegendRow(c.catBlue, "Transport", "26%")
                        LegendRow(c.catPurple, "Bills", "18%")
                    }
                }
            }
            PreviewCard(Modifier.fillMaxWidth()) {
                Column {
                    Text("Net worth · 6 months", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(Spacing.s3))
                    Canvas(Modifier.fillMaxWidth().height(64.dp)) {
                        val pts = listOf(0.74f, 0.62f, 0.68f, 0.42f, 0.48f, 0.26f, 0.16f)
                        val stepX = size.width / (pts.size - 1)
                        val line = Path().apply {
                            pts.forEachIndexed { i, v -> if (i == 0) moveTo(0f, v * size.height) else lineTo(i * stepX, v * size.height) }
                        }
                        val fill = Path().apply {
                            addPath(line); lineTo(size.width, size.height); lineTo(0f, size.height); close()
                        }
                        clipRect(right = size.width * p) {
                            drawPath(fill, Brush.verticalGradient(listOf(accent.copy(alpha = 0.22f), accent.copy(alpha = 0f))))
                            drawPath(line, accent, style = Stroke(width = 6f, cap = StrokeCap.Round))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendRow(color: Color, label: String, pct: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(10.dp).clip(RoundedCornerShape(3.dp)).background(color))
        Spacer(Modifier.width(Spacing.s3))
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.weight(1f))
        Text(pct, style = HisabakType.amount.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/* --------------------------------- page 5 ---------------------------------- */

@Composable
fun GetStartedPage(active: Boolean, parallax: Float) {
    val p = progressFor(active)
    val c = HisabakTheme.colors
    OnboardingPage(
        active, parallax,
        overline = "You're all set",
        title = "Ready when you are.",
        subtitle = "Turn on SMS auto-capture to log transactions the moment they happen — or add them by hand anytime.",
    ) {
        Column(Modifier.width(320.dp), verticalArrangement = Arrangement.spacedBy(Spacing.s3)) {
            RecapRow(p, 0f, Icons.Filled.Bolt, c.income, c.incomeSoft, "Automatic capture", "from your bank SMS")
            RecapRow(p, 0.15f, Icons.Filled.Savings, c.warning, c.warningSoft, "Smart budgets", "alerts before you overshoot")
            RecapRow(p, 0.30f, Icons.Filled.Insights, c.savings, c.savings.copy(alpha = 0.15f), "Clear insights", "trends, categories, brands")
            Spacer(Modifier.height(Spacing.s2))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Spacing.s5))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f))
                    .padding(Spacing.s5),
            ) {
                Box(
                    Modifier.size(44.dp).clip(TileShape).background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center,
                ) { Icon(Icons.Filled.Sms, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) }
                Spacer(Modifier.width(Spacing.s4))
                Column(Modifier.weight(1f)) {
                    Text("Turn on SMS auto-capture", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        "We only read bank transaction alerts. Nothing leaves your device.",
                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun RecapRow(p: Float, delay: Float, icon: ImageVector, fg: Color, bg: Color, title: String, sub: String) {
    val a = ((p - delay) / (1f - delay)).coerceIn(0f, 1f)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { alpha = a }
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
            .padding(Spacing.s4),
    ) {
        Box(Modifier.size(44.dp).clip(TileShape).background(bg), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = fg, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(Spacing.s4))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
            Text(sub, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
