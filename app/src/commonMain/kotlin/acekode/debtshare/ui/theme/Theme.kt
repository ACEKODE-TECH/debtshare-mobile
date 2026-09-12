package acekode.debtshare.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

private val LightColorScheme = lightColorScheme(
    primary = DebtshareColors.Brand.primary,
    onPrimary = DebtshareColors.Neutral.n0,
    secondary = DebtshareColors.Brand.secondary,
    background = DebtshareColors.Neutral.n50,
    onBackground = DebtshareColors.Neutral.n900,
    surface = DebtshareColors.Neutral.n0,
    onSurface = DebtshareColors.Neutral.n900,
    error = DebtshareColors.Semantic.error,
    onError = DebtshareColors.Neutral.n0,
)

private val DarkColorScheme = darkColorScheme(
    primary = DebtshareColors.Brand.primary,
    onPrimary = DebtshareColors.Neutral.n0,
    secondary = DebtshareColors.Brand.secondary,
    background = DebtshareColors.Neutral.n900,
    onBackground = DebtshareColors.Neutral.n100,
    surface = DebtshareColors.Neutral.n850,
    onSurface = DebtshareColors.Neutral.n100,
    error = DebtshareColors.Semantic.error,
    onError = DebtshareColors.Neutral.n0,
)

val LocalDebtshareColors = staticCompositionLocalOf { LightDebtshareColorScheme }

@Composable
fun DebtshareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val debtshareColors = if (darkTheme) DarkDebtshareColorScheme else LightDebtshareColorScheme
    val typography = remember { debtshareTypography() }
    val spacing = remember { DebtshareSpacing() }
    val radius = remember { DebtshareRadius() }
    val elevation = remember { DebtshareElevation() }

    CompositionLocalProvider(
        LocalDebtshareColors provides debtshareColors,
        LocalDebtshareTypography provides typography,
        LocalDebtshareSpacing provides spacing,
        LocalDebtshareRadius provides radius,
        LocalDebtshareElevation provides elevation,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography.toMaterialTypography(),
            content = content,
        )
    }
}

object DebtshareTheme {

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalDebtshareTypography.current

    val spacing: DebtshareSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalDebtshareSpacing.current

    val radius: DebtshareRadius
        @Composable
        @ReadOnlyComposable
        get() = LocalDebtshareRadius.current

    val elevation: DebtshareElevation
        @Composable
        @ReadOnlyComposable
        get() = LocalDebtshareElevation.current

    val colors: DebtshareColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalDebtshareColors.current
}
