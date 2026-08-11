package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import acekode.debtshare.ui.utils.PreviewGallery
import acekode.debtshare.ui.utils.PreviewLabel
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class DebtshareBadgeVariant { Neutral, Brand, Success, Warning, Danger, Plum, SolidDanger }

enum class DebtshareBadgeSize { Small, Medium, Large }

object DebtshareBadgeDefaults {
    val heightSmall: Dp = 8.dp
    val heightMedium: Dp = 16.dp
    val heightLarge: Dp = 32.dp
    val dot: Dp = 4.dp
    const val UPPERCASE_TRACKING = 0.5f
}

@Composable
fun DebtshareBadge(
    text: String,
    modifier: Modifier = Modifier,
    variant: DebtshareBadgeVariant = DebtshareBadgeVariant.Neutral,
    size: DebtshareBadgeSize = DebtshareBadgeSize.Medium,
    uppercase: Boolean = false,
    showDot: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val palette = badgeColors(variant)
    val size = size.badgeSize()
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(size.radius))
            .background(palette.container)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            )
            .defaultMinSize(minHeight = size.minHeight)
            .padding(horizontal = size.horizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.verySmall),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showDot) {
            Box(
                modifier = Modifier
                    .size(DebtshareBadgeDefaults.dot)
                    .clip(CircleShape)
                    .background(palette.content),
            )
        }
        Text(
            text = if (uppercase) text.uppercase() else text,
            style = size.textStyle,
            color = palette.content,
            maxLines = 1,
            overflow = TextOverflow.Clip,
        )
    }
}

@Immutable
private data class BadgePalette(val container: Color, val content: Color)

@Immutable
private data class BadgeSize(
    val minHeight: Dp,
    val horizontalPadding: Dp,
    val radius: Dp,
    val textStyle: TextStyle,
)

@Composable
private fun badgeColors(variant: DebtshareBadgeVariant): BadgePalette = when (variant) {
    DebtshareBadgeVariant.Neutral -> BadgePalette(
        container = DebtshareTheme.colors.neutralTint,
        content = DebtshareTheme.colors.textSecondary,
    )

    DebtshareBadgeVariant.Brand -> BadgePalette(
        container = DebtshareColors.Brand.primaryTint,
        content = DebtshareColors.Brand.primary,
    )

    DebtshareBadgeVariant.Success -> BadgePalette(
        container = DebtshareColors.Semantic.successTintSoft,
        content = DebtshareColors.Semantic.success,
    )

    DebtshareBadgeVariant.Warning -> BadgePalette(
        container = DebtshareColors.Accent.mustardTint,
        content = DebtshareColors.Accent.mustardDark,
    )

    DebtshareBadgeVariant.Danger -> BadgePalette(
        container = DebtshareColors.Semantic.errorTintSoft,
        content = DebtshareColors.Semantic.error,
    )

    DebtshareBadgeVariant.Plum -> BadgePalette(
        container = DebtshareColors.Accent.plumTint,
        content = DebtshareColors.Accent.plum,
    )

    DebtshareBadgeVariant.SolidDanger -> BadgePalette(
        container = DebtshareColors.Semantic.error,
        content = DebtshareColors.Neutral.n0,
    )
}

@Composable
private fun DebtshareBadgeSize.badgeSize(): BadgeSize = when (this) {
    DebtshareBadgeSize.Small -> BadgeSize(
        minHeight = DebtshareBadgeDefaults.heightSmall,
        horizontalPadding = DebtshareTheme.spacing.verySmall,
        radius = DebtshareTheme.radius.verySmall,
        textStyle = DebtshareTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = DebtshareBadgeDefaults.UPPERCASE_TRACKING.sp,
        ),
    )

    DebtshareBadgeSize.Medium -> BadgeSize(
        minHeight = DebtshareBadgeDefaults.heightMedium,
        horizontalPadding = DebtshareTheme.spacing.small,
        radius = DebtshareTheme.radius.verySmall,
        textStyle = DebtshareTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = DebtshareBadgeDefaults.UPPERCASE_TRACKING.sp,
        ),
    )

    DebtshareBadgeSize.Large -> BadgeSize(
        minHeight = DebtshareBadgeDefaults.heightLarge,
        horizontalPadding = DebtshareTheme.spacing.medium,
        radius = DebtshareTheme.radius.small,
        textStyle = DebtshareTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = DebtshareBadgeDefaults.UPPERCASE_TRACKING.sp,
        ),
    )
}

@Composable
private fun BadgeVariantsGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Variantes")
        Row(
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareBadge(text = "5 grupos")
            DebtshareBadge(text = "Grupo", variant = DebtshareBadgeVariant.Brand)
            DebtshareBadge(
                text = "Saldado",
                variant = DebtshareBadgeVariant.Success,
                uppercase = true,
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareBadge(
                text = "Pendiente",
                variant = DebtshareBadgeVariant.Warning,
                uppercase = true,
            )
            DebtshareBadge(text = "Vencido", variant = DebtshareBadgeVariant.Danger, uppercase = true)
            DebtshareBadge(text = "Viaje", variant = DebtshareBadgeVariant.Plum)
            DebtshareBadge(text = "3", variant = DebtshareBadgeVariant.SolidDanger)
        }
        PreviewLabel("Con punto")
        Row(
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareBadge(text = "Activo", variant = DebtshareBadgeVariant.Success, showDot = true)
            DebtshareBadge(text = "Pendiente", variant = DebtshareBadgeVariant.Warning, showDot = true)
            DebtshareBadge(text = "Nuevo", variant = DebtshareBadgeVariant.Brand, showDot = true)
        }
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareBadgeVariantsPreview() {
    BadgeVariantsGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareBadgeVariantsDarkPreview() {
    BadgeVariantsGallery(darkTheme = true)
}

@Composable
private fun BadgeSizesGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Small · Medium · Large")
        Row(
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareBadge(text = "SALDADO", size = DebtshareBadgeSize.Small, variant = DebtshareBadgeVariant.Success)
            DebtshareBadge(text = "SALDADO", size = DebtshareBadgeSize.Medium, variant = DebtshareBadgeVariant.Success)
            DebtshareBadge(text = "SALDADO", size = DebtshareBadgeSize.Large, variant = DebtshareBadgeVariant.Success)
        }
        PreviewLabel("Clickable (chip filtro)")
        Row(horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.small)) {
            DebtshareBadge(text = "Todos", variant = DebtshareBadgeVariant.Brand, onClick = {})
            DebtshareBadge(text = "Pendientes", onClick = {})
            DebtshareBadge(text = "Saldados", onClick = {})
        }
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareBadgeSizesPreview() {
    BadgeSizesGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareBadgeSizesDarkPreview() {
    BadgeSizesGallery(darkTheme = true)
}
