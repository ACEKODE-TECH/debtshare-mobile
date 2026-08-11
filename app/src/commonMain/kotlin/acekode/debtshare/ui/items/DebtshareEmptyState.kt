package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import acekode.debtshare.ui.utils.PreviewGallery
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.alert
import debtshare.app.generated.resources.check_circle
import debtshare.app.generated.resources.inbox
import debtshare.app.generated.resources.search
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class DebtshareEmptyStateVariant { Neutral, Success, Error, Search }

@Stable
data class DebtshareEmptyStateAction(
    val label: String,
    val onClick: () -> Unit,
)

object DebtshareEmptyStateDefaults {
    val iconContainer: Dp = 64.dp
    val iconSize: Dp = 32.dp
    const val DESCRIPTION_MAX_LINES = 2
}

@Composable
fun DebtshareEmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    variant: DebtshareEmptyStateVariant = DebtshareEmptyStateVariant.Neutral,
    primaryAction: DebtshareEmptyStateAction? = null,
    secondaryAction: DebtshareEmptyStateAction? = null,
) {
    val style = variant.style()
    val textPrimary = DebtshareTheme.colors.textPrimary
    val textTertiary = DebtshareTheme.colors.textTertiary

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = DebtshareTheme.spacing.large,
                vertical = DebtshareTheme.spacing.veryLarge,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EmptyStateGlyph(style = style)
        Spacer(Modifier.height(DebtshareTheme.spacing.large))
        Text(
            text = title,
            style = DebtshareTheme.typography.displaySmall,
            color = textPrimary,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(DebtshareTheme.spacing.small))
        Text(
            text = description,
            style = DebtshareTheme.typography.bodyMedium,
            color = textTertiary,
            textAlign = TextAlign.Center,
            maxLines = DebtshareEmptyStateDefaults.DESCRIPTION_MAX_LINES,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth().padding(horizontal = DebtshareTheme.spacing.large),
        )
        EmptyStateActions(
            variant = variant,
            primaryAction = primaryAction,
            secondaryAction = secondaryAction,
        )
    }
}

@Composable
private fun EmptyStateGlyph(style: EmptyStateStyle) {
    Box(
        modifier = Modifier
            .size(DebtshareEmptyStateDefaults.iconContainer)
            .clip(CircleShape)
            .background(style.container),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(style.icon),
            contentDescription = null,
            tint = style.content,
            modifier = Modifier.size(DebtshareEmptyStateDefaults.iconSize),
        )
    }
}

@Composable
private fun EmptyStateActions(
    variant: DebtshareEmptyStateVariant,
    primaryAction: DebtshareEmptyStateAction?,
    secondaryAction: DebtshareEmptyStateAction?,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (primaryAction != null) {
            Spacer(Modifier.height(DebtshareTheme.spacing.large))
            DebtshareButton(
                text = primaryAction.label,
                onClick = primaryAction.onClick,
                variant = if (variant == DebtshareEmptyStateVariant.Error) {
                    DebtshareButtonVariant.Secondary
                } else {
                    DebtshareButtonVariant.Primary
                },
            )
        }
        if (secondaryAction != null) {
            Spacer(Modifier.height(DebtshareTheme.spacing.medium))
            DebtshareButton(
                text = secondaryAction.label,
                onClick = secondaryAction.onClick,
                variant = DebtshareButtonVariant.Ghost,
                size = DebtshareButtonSize.Small,
            )
        }
    }
}

@Immutable
private data class EmptyStateStyle(val container: Color, val content: Color, val icon: DrawableResource)

@Composable
private fun DebtshareEmptyStateVariant.style(): EmptyStateStyle = when (this) {
    DebtshareEmptyStateVariant.Neutral -> EmptyStateStyle(
        container = DebtshareTheme.colors.neutralTint,
        content = DebtshareTheme.colors.textTertiary,
        icon = Res.drawable.inbox,
    )

    DebtshareEmptyStateVariant.Success -> EmptyStateStyle(
        container = DebtshareColors.Semantic.successTintSoft,
        content = DebtshareColors.Semantic.success,
        icon = Res.drawable.check_circle,
    )

    DebtshareEmptyStateVariant.Error -> EmptyStateStyle(
        container = DebtshareColors.Semantic.errorTintSoft,
        content = DebtshareColors.Semantic.error,
        icon = Res.drawable.alert,
    )

    DebtshareEmptyStateVariant.Search -> EmptyStateStyle(
        container = DebtshareColors.Brand.primaryTint,
        content = DebtshareColors.Brand.primary,
        icon = Res.drawable.search,
    )
}

@Composable
private fun EmptyStateNeutralGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        DebtshareEmptyState(
            title = "Aún no hay gastos",
            description = "Añade el primer gasto del grupo y empezaremos a repartir el balance.",
            primaryAction = DebtshareEmptyStateAction(label = "Añadir gasto", onClick = {}),
            secondaryAction = DebtshareEmptyStateAction(label = "Invitar a alguien", onClick = {}),
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareEmptyStateNeutralPreview() {
    EmptyStateNeutralGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareEmptyStateNeutralDarkPreview() {
    EmptyStateNeutralGallery(darkTheme = true)
}

@Composable
private fun EmptyStateSuccessGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        DebtshareEmptyState(
            title = "Todo saldado",
            description = "Nadie debe nada en este grupo. Buen trabajo.",
            variant = DebtshareEmptyStateVariant.Success,
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareEmptyStateSuccessPreview() {
    EmptyStateSuccessGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareEmptyStateSuccessDarkPreview() {
    EmptyStateSuccessGallery(darkTheme = true)
}

@Composable
private fun EmptyStateErrorGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        DebtshareEmptyState(
            title = "No hemos podido cargar los gastos",
            description = "Revisa tu conexión e inténtalo de nuevo.",
            variant = DebtshareEmptyStateVariant.Error,
            primaryAction = DebtshareEmptyStateAction(label = "Reintentar", onClick = {}),
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareEmptyStateErrorPreview() {
    EmptyStateErrorGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareEmptyStateErrorDarkPreview() {
    EmptyStateErrorGallery(darkTheme = true)
}

@Composable
private fun EmptyStateSearchGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        DebtshareEmptyState(
            title = "Sin resultados",
            description = "No encontramos gastos que coincidan con «playa».",
            variant = DebtshareEmptyStateVariant.Search,
            secondaryAction = DebtshareEmptyStateAction(label = "Limpiar filtros", onClick = {}),
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareEmptyStateSearchPreview() {
    EmptyStateSearchGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareEmptyStateSearchDarkPreview() {
    EmptyStateSearchGallery(darkTheme = true)
}
