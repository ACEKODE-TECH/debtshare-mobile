package acekode.debtshare.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Immutable
data class DebtshareTypography(
    val displaySmall: TextStyle,
    val displayMedium: TextStyle,
    val displayLarge: TextStyle,
    val bodySmall: TextStyle,
    val bodyMedium: TextStyle,
    val bodyLarge: TextStyle,
    val monoSmall: TextStyle,
    val monoMedium: TextStyle,
    val monoLarge: TextStyle,
    val labelUppercase: TextStyle,
)

private val LineHeight = 1.5.em

private fun display(size: Double, tracking: Double): TextStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.ExtraBold,
    fontSize = size.sp,
    lineHeight = LineHeight,
    letterSpacing = tracking.sp,
)

private fun body(size: Double): TextStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = size.sp,
    lineHeight = LineHeight,
)

private fun mono(size: Double): TextStyle = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontWeight = FontWeight.Normal,
    fontSize = size.sp,
    lineHeight = LineHeight,
)

fun debtshareTypography(): DebtshareTypography = DebtshareTypography(
    displaySmall = display(size = 20.0, tracking = -0.3),
    displayMedium = display(size = 30.0, tracking = -1.0),
    displayLarge = display(size = 42.0, tracking = -1.0),
    bodySmall = body(size = 11.0),
    bodyMedium = body(size = 13.0),
    bodyLarge = body(size = 16.0),
    monoSmall = mono(size = 10.0),
    monoMedium = mono(size = 11.0),
    monoLarge = mono(size = 12.0),
    labelUppercase = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 10.5.sp,
        lineHeight = LineHeight,
        letterSpacing = 0.4.sp,
    ),
)

internal fun DebtshareTypography.toMaterialTypography(): Typography = Typography(
    displayLarge = displayLarge,
    displayMedium = displayMedium,
    displaySmall = displaySmall,
    headlineLarge = displayMedium,
    headlineMedium = displaySmall,
    headlineSmall = displaySmall,
    titleLarge = bodyLarge,
    titleMedium = bodyLarge,
    titleSmall = bodyMedium,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = bodyMedium,
    labelMedium = bodySmall,
    labelSmall = bodySmall,
)

val LocalDebtshareTypography = staticCompositionLocalOf { debtshareTypography() }
