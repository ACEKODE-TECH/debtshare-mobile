package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareColorScheme
import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareElevation
import acekode.debtshare.ui.theme.DebtshareTheme
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp

@Immutable
internal data class ButtonStyle(
    val container: Color,
    val content: Color,
    val border: Color?,
    val ring: Color,
    val elevation: Dp,
)

@Immutable
internal data class ButtonSize(
    val minHeight: Dp,
    val horizontalPadding: Dp,
    val iconSize: Dp,
    val textStyle: TextStyle,
)

internal fun Modifier.buttonOutline(focused: Boolean, style: ButtonStyle, shape: Shape): Modifier = when {
    focused -> border(DebtshareButtonDefaults.focusRingWidth, style.ring, shape)
    style.border != null -> border(DebtshareButtonDefaults.borderWidth, style.border, shape)
    else -> this
}

@Composable
internal fun DebtshareButtonSize.buttonSize(): ButtonSize = when (this) {
    DebtshareButtonSize.Small -> ButtonSize(
        minHeight = DebtshareButtonDefaults.heightSmall,
        horizontalPadding = DebtshareTheme.spacing.medium,
        iconSize = DebtshareButtonDefaults.iconSmall,
        textStyle = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
    )

    DebtshareButtonSize.Medium -> ButtonSize(
        minHeight = DebtshareButtonDefaults.heightMedium,
        horizontalPadding = DebtshareTheme.spacing.large,
        iconSize = DebtshareButtonDefaults.iconMedium,
        textStyle = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
    )

    DebtshareButtonSize.Large -> ButtonSize(
        minHeight = DebtshareButtonDefaults.heightLarge,
        horizontalPadding = DebtshareTheme.spacing.veryLarge,
        iconSize = DebtshareButtonDefaults.iconLarge,
        textStyle = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
    )
}

@Composable
internal fun buttonStyle(
    variant: DebtshareButtonVariant,
    enabled: Boolean,
): ButtonStyle {
    val colors = DebtshareTheme.colors
    val elevation = DebtshareTheme.elevation
    return when (variant) {
        DebtshareButtonVariant.Primary -> primaryStyle(colors, elevation, enabled)
        DebtshareButtonVariant.Secondary -> secondaryStyle(colors, elevation, enabled)
        DebtshareButtonVariant.Ghost -> ghostStyle(colors, elevation, enabled)
        DebtshareButtonVariant.Destructive -> destructiveStyle(colors, elevation, enabled)
    }
}

private fun primaryStyle(
    colors: DebtshareColorScheme,
    elevation: DebtshareElevation,
    enabled: Boolean,
): ButtonStyle = ButtonStyle(
    container = if (enabled) DebtshareColors.Brand.primary else colors.border,
    content = if (enabled) DebtshareColors.Neutral.n0 else DebtshareColors.Neutral.n400,
    border = null,
    ring = DebtshareColors.Brand.primaryLight,
    elevation = if (enabled) elevation.large else elevation.none,
)

private fun secondaryStyle(
    colors: DebtshareColorScheme,
    elevation: DebtshareElevation,
    enabled: Boolean,
): ButtonStyle = ButtonStyle(
    container = if (enabled) {
        colors.card
    } else {
        colors.card.copy(alpha = DebtshareButtonDefaults.DISABLED_ALPHA_SECONDARY)
    },
    content = if (enabled) {
        colors.textPrimary
    } else {
        DebtshareColors.Neutral.n400
    },
    border = colors.border,
    ring = DebtshareColors.Brand.primary,
    elevation = elevation.none,
)

private fun ghostStyle(
    colors: DebtshareColorScheme,
    elevation: DebtshareElevation,
    enabled: Boolean,
): ButtonStyle = ButtonStyle(
    container = Color.Transparent,
    content = if (enabled) {
        colors.textSecondary
    } else {
        colors.textSecondary.copy(alpha = DebtshareButtonDefaults.DISABLED_ALPHA_GHOST)
    },
    border = null,
    ring = DebtshareColors.Brand.primary,
    elevation = elevation.none,
)

private fun destructiveStyle(
    colors: DebtshareColorScheme,
    elevation: DebtshareElevation,
    enabled: Boolean,
): ButtonStyle = ButtonStyle(
    container = if (enabled) DebtshareColors.Semantic.error else colors.disabledDestructiveContainer,
    content = if (enabled) {
        DebtshareColors.Neutral.n0
    } else {
        DebtshareColors.Semantic.error.copy(alpha = DebtshareButtonDefaults.DISABLED_ALPHA_SECONDARY)
    },
    border = null,
    ring = DebtshareColors.Semantic.errorLight,
    elevation = elevation.none,
)
