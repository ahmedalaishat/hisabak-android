package com.hisabak.feature.category.presentation

import androidx.compose.ui.graphics.Color

/**
 * Shared color and icon vocabulary for Category presentation.
 * Categories store color/icon as short keys (e.g. "green", "cart"); this maps
 * them to Compose primitives. Unknown keys fall back to a neutral default.
 */
object CategoryStyle {
    val palette: List<String> = listOf(
        "green", "blue", "orange", "red", "teal", "purple", "pink", "gray",
    )

    val icons: List<String> = listOf(
        "wallet", "cart", "briefcase", "car", "utensils", "piggy-bank",
        "home", "film", "book", "heart", "gift", "plane",
    )

    fun color(key: String?): Color = when (key) {
        "green" -> Color(0xFF2E7D32)
        "blue" -> Color(0xFF1565C0)
        "orange" -> Color(0xFFEF6C00)
        "red" -> Color(0xFFC62828)
        "teal" -> Color(0xFF00838F)
        "purple" -> Color(0xFF6A1B9A)
        "pink" -> Color(0xFFAD1457)
        "gray" -> Color(0xFF616161)
        else -> Color(0xFF616161)
    }
}
