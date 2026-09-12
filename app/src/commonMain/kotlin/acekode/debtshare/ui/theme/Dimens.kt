package acekode.debtshare.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class DebtshareSpacing(
    val verySmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val veryLarge: Dp = 32.dp,
)

@Immutable
data class DebtshareRadius(
    val verySmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val veryLarge: Dp = 32.dp,
)

@Immutable
data class DebtshareElevation(
    val none: Dp = 0.dp,
    val verySmall: Dp = 1.dp,
    val small: Dp = 2.dp,
    val medium: Dp = 6.dp,
    val large: Dp = 10.dp,
)

val LocalDebtshareSpacing = staticCompositionLocalOf { DebtshareSpacing() }
val LocalDebtshareRadius = staticCompositionLocalOf { DebtshareRadius() }
val LocalDebtshareElevation = staticCompositionLocalOf { DebtshareElevation() }
