package com.hisabak.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hisabak.ui.theme.TintBlue
import com.hisabak.ui.theme.TintBlueOn
import com.hisabak.ui.theme.TintEmerald
import com.hisabak.ui.theme.TintEmeraldOn
import com.hisabak.ui.theme.TintGray
import com.hisabak.ui.theme.TintGrayOn
import com.hisabak.ui.theme.TintOrange
import com.hisabak.ui.theme.TintOrangeOn
import com.hisabak.ui.theme.TintPink
import com.hisabak.ui.theme.TintPinkOn
import com.hisabak.ui.theme.TintPurple
import com.hisabak.ui.theme.TintPurpleOn
import com.hisabak.ui.theme.TintRed
import com.hisabak.ui.theme.TintRedOn
import com.hisabak.ui.theme.TintTeal
import com.hisabak.ui.theme.TintTealOn

/**
 * Rounded tile with a tinted background and a centered icon. Used for category
 * avatars, list-row leading icons, and small stat indicators.
 */
@Composable
fun IconTile(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    iconSize: Dp = 20.dp,
    background: Color = TintGray,
    foreground: Color = TintGrayOn,
    shape: Shape = RoundedCornerShape(10.dp),
    contentDescription: String? = null,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(background, shape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = foreground,
            modifier = Modifier.size(iconSize),
        )
    }
}

@Composable
fun CircleIconTile(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    iconSize: Dp = 24.dp,
    background: Color = TintGray,
    foreground: Color = TintGrayOn,
    contentDescription: String? = null,
) = IconTile(
    icon = icon,
    modifier = modifier,
    size = size,
    iconSize = iconSize,
    background = background,
    foreground = foreground,
    shape = CircleShape,
    contentDescription = contentDescription,
)

/** Maps the app's category color keys to (background, foreground) pairs. */
fun tintPairForColor(key: String?): Pair<Color, Color> = when (key) {
    "green" -> TintEmerald to TintEmeraldOn
    "blue" -> TintBlue to TintBlueOn
    "orange" -> TintOrange to TintOrangeOn
    "red" -> TintRed to TintRedOn
    "teal" -> TintTeal to TintTealOn
    "purple" -> TintPurple to TintPurpleOn
    "pink" -> TintPink to TintPinkOn
    else -> TintGray to TintGrayOn
}

/** Maps category icon keys (persisted as short strings) to Material vectors. */
fun iconForKey(key: String?): ImageVector = when (key) {
    "wallet" -> Icons.Filled.AccountBalanceWallet
    "cart" -> Icons.Filled.ShoppingCart
    "briefcase" -> Icons.Filled.Work
    "car" -> Icons.Filled.DirectionsCar
    "utensils" -> Icons.Filled.Restaurant
    "piggy-bank" -> Icons.Filled.Savings
    "home" -> Icons.Filled.Home
    "film" -> Icons.Filled.Movie
    "book" -> Icons.Filled.MenuBook
    "heart" -> Icons.Filled.Favorite
    "gift" -> Icons.Filled.CardGiftcard
    "plane" -> Icons.Filled.FlightTakeoff
    else -> Icons.Filled.Circle
}
