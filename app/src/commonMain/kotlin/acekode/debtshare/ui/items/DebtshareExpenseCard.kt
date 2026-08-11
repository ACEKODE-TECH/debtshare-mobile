package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import acekode.debtshare.ui.utils.PreviewGallery
import acekode.debtshare.ui.utils.PreviewLabel
import acekode.debtshare.ui.utils.formatEuros
import acekode.debtshare.ui.utils.formatSignedEuros
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.bag
import debtshare.app.generated.resources.car
import debtshare.app.generated.resources.cup
import debtshare.app.generated.resources.dots
import debtshare.app.generated.resources.house
import debtshare.app.generated.resources.no_impact
import debtshare.app.generated.resources.settled
import debtshare.app.generated.resources.star
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs

enum class ExpenseCategory { FoodAndDrink, Transport, Lodging, Leisure, Shopping, Other }

enum class ExpenseCardVariant { Default, Compact, Settled }

object ExpenseCardDefaults {
    val minHeight: Dp = 64.dp
    val compactMinHeight: Dp = 64.dp
    val iconBox: Dp = 48.dp
    val compactIconBox: Dp = 32.dp
    val iconSize: Dp = 24.dp
    val compactGlyph: Dp = 18.dp
    val borderWidth: Dp = 1.dp
    val focusRingWidth: Dp = 2.dp
    const val SETTLED_ICON_ALPHA = 0.5f
    const val ZERO_EPSILON = 0.005
}

@Composable
fun DebtshareExpenseCard(
    title: String,
    subtitle: String,
    amount: Double,
    personalDelta: Double,
    modifier: Modifier = Modifier,
    category: ExpenseCategory = ExpenseCategory.Other,
    variant: ExpenseCardVariant = ExpenseCardVariant.Default,
    onClick: (() -> Unit)? = null,
) {
    val compact = variant == ExpenseCardVariant.Compact
    val settled = variant == ExpenseCardVariant.Settled
    val shape = RoundedCornerShape(DebtshareTheme.radius.large)
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    Row(
        modifier = modifier.then(
            expenseCardContainerModifier(
                compact = compact,
                shape = shape,
                focused = focused,
                onClick = onClick,
                interactionSource = interactionSource,
            ),
        ),
        horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CategoryGlyph(category = category, compact = compact, settled = settled)
        ExpenseTexts(
            title = title,
            subtitle = subtitle,
            compact = compact,
            settled = settled,
            modifier = Modifier.weight(1f),
        )
        ExpenseAmounts(amount = amount, personalDelta = personalDelta, settled = settled)
    }
}

@Composable
private fun expenseCardContainerModifier(
    compact: Boolean,
    shape: Shape,
    focused: Boolean,
    onClick: (() -> Unit)?,
    interactionSource: MutableInteractionSource,
): Modifier {
    val card = DebtshareTheme.colors.card
    val cardBorder = DebtshareTheme.colors.neutralTint

    return Modifier
        .fillMaxWidth()
        .defaultMinSize(
            minHeight = if (compact) ExpenseCardDefaults.compactMinHeight else ExpenseCardDefaults.minHeight,
        )
        .clip(shape)
        .background(card)
        .border(
            width = if (focused) ExpenseCardDefaults.focusRingWidth else ExpenseCardDefaults.borderWidth,
            color = if (focused) DebtshareColors.Brand.primary else cardBorder,
            shape = shape,
        )
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
        .padding(
            horizontal = if (compact) DebtshareTheme.spacing.medium else DebtshareTheme.spacing.large,
            vertical = if (compact) DebtshareTheme.spacing.medium else DebtshareTheme.spacing.large,
        )
}

@Composable
private fun CategoryGlyph(category: ExpenseCategory, compact: Boolean, settled: Boolean) {
    val style = category.style()
    val boxSize = if (compact) ExpenseCardDefaults.compactIconBox else ExpenseCardDefaults.iconBox
    val glyphSize = if (compact) ExpenseCardDefaults.compactGlyph else ExpenseCardDefaults.iconSize

    Box(
        modifier = Modifier
            .size(boxSize)
            .alpha(if (settled) ExpenseCardDefaults.SETTLED_ICON_ALPHA else 1f)
            .clip(RoundedCornerShape(DebtshareTheme.radius.medium))
            .background(style.container),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(style.icon),
            contentDescription = null,
            tint = style.content,
            modifier = Modifier.size(glyphSize),
        )
    }
}

@Composable
private fun ExpenseTexts(
    title: String,
    subtitle: String,
    compact: Boolean,
    settled: Boolean,
    modifier: Modifier = Modifier,
) {
    val textPrimary = DebtshareTheme.colors.textPrimary
    val textTertiary = DebtshareTheme.colors.textTertiary
    val textMuted = DebtshareTheme.colors.textMuted

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.verySmall),
    ) {
        Text(
            text = title,
            style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = if (settled) textMuted else textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = subtitle,
            style = DebtshareTheme.typography.bodySmall,
            color = if (settled) textMuted else textTertiary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (settled) {
            DebtshareBadge(
                text = stringResource(Res.string.settled),
                size = DebtshareBadgeSize.Small,
                uppercase = true,
            )
        }
    }
}

@Composable
private fun ExpenseAmounts(amount: Double, personalDelta: Double, settled: Boolean) {
    val textPrimary = DebtshareTheme.colors.textPrimary
    val textTertiary = DebtshareTheme.colors.textTertiary
    val textMuted = DebtshareTheme.colors.textMuted
    val neutralDelta = abs(personalDelta) < ExpenseCardDefaults.ZERO_EPSILON

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.verySmall),
    ) {
        Text(
            text = formatEuros(amount),
            style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = if (settled) textMuted else textPrimary,
            maxLines = 1,
        )
        Text(
            text = if (neutralDelta) stringResource(Res.string.no_impact) else formatSignedEuros(personalDelta),
            style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = when {
                settled || neutralDelta -> textTertiary
                personalDelta > 0 -> DebtshareColors.Semantic.success
                else -> DebtshareColors.Semantic.error
            },
            maxLines = 1,
        )
    }
}

@Immutable
private data class CategoryStyle(val container: Color, val content: Color, val icon: DrawableResource)

@Composable
private fun ExpenseCategory.style(): CategoryStyle = when (this) {
    ExpenseCategory.FoodAndDrink -> CategoryStyle(
        container = DebtshareColors.Accent.mustardTint,
        content = DebtshareColors.Accent.mustardDark,
        icon = Res.drawable.cup,
    )

    ExpenseCategory.Transport -> CategoryStyle(
        container = DebtshareColors.Brand.primaryTint,
        content = DebtshareColors.Brand.primary,
        icon = Res.drawable.car,
    )

    ExpenseCategory.Lodging -> CategoryStyle(
        container = DebtshareColors.Accent.plumTint,
        content = DebtshareColors.Accent.plum,
        icon = Res.drawable.house,
    )

    ExpenseCategory.Leisure -> CategoryStyle(
        container = DebtshareColors.Neutral.n100,
        content = DebtshareColors.Neutral.n600,
        icon = Res.drawable.star,
    )

    ExpenseCategory.Shopping -> CategoryStyle(
        container = DebtshareColors.Semantic.successTintSoft,
        content = DebtshareColors.Semantic.success,
        icon = Res.drawable.bag,
    )

    ExpenseCategory.Other -> CategoryStyle(
        container = DebtshareColors.Neutral.n100,
        content = DebtshareColors.Neutral.n500,
        icon = Res.drawable.dots,
    )
}

private const val DINNER_AMOUNT = 128.4
private const val DINNER_DELTA = 32.1
private const val TAXI_AMOUNT = 1284.5
private const val TAXI_DELTA = -21.4
private const val HOTEL_AMOUNT = 640.0

@Composable
private fun ExpenseCardCategoriesGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Categories")
        DebtshareExpenseCard(
            title = "Dinner at Casa Paco",
            subtitle = "Paid by Maria · 2h ago",
            amount = DINNER_AMOUNT,
            personalDelta = DINNER_DELTA,
            category = ExpenseCategory.FoodAndDrink,
            onClick = {},
        )
        DebtshareExpenseCard(
            title = "Flights Madrid–Reykjavik",
            subtitle = "Paid by Jorge · yesterday",
            amount = TAXI_AMOUNT,
            personalDelta = TAXI_DELTA,
            category = ExpenseCategory.Transport,
            onClick = {},
        )
        DebtshareExpenseCard(
            title = "Downtown apartment",
            subtitle = "Paid by Ana · 3 days ago",
            amount = HOTEL_AMOUNT,
            personalDelta = 0.0,
            category = ExpenseCategory.Lodging,
            onClick = {},
        )
        DebtshareExpenseCard(
            title = "Concert tickets",
            subtitle = "Paid by Carlos · 1 week ago",
            amount = DINNER_AMOUNT,
            personalDelta = DINNER_DELTA,
            category = ExpenseCategory.Leisure,
        )
        DebtshareExpenseCard(
            title = "Weekly groceries",
            subtitle = "Paid by Lucia · today",
            amount = DINNER_AMOUNT,
            personalDelta = -DINNER_DELTA,
            category = ExpenseCategory.Shopping,
        )
        DebtshareExpenseCard(
            title = "Misc",
            subtitle = "Paid by Jorge · 1 month ago",
            amount = DINNER_AMOUNT,
            personalDelta = DINNER_DELTA,
            category = ExpenseCategory.Other,
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun ExpenseCardCategoriesPreview() {
    ExpenseCardCategoriesGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun ExpenseCardCategoriesDarkPreview() {
    ExpenseCardCategoriesGallery(darkTheme = true)
}

@Composable
private fun ExpenseCardVariantsGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Default")
        DebtshareExpenseCard(
            title = "Dinner at Casa Paco",
            subtitle = "Paid by Maria · 2h ago",
            amount = DINNER_AMOUNT,
            personalDelta = DINNER_DELTA,
            category = ExpenseCategory.FoodAndDrink,
            onClick = {},
        )
        PreviewLabel("Compact")
        DebtshareExpenseCard(
            title = "Dinner at Casa Paco",
            subtitle = "Paid by Maria · 2h ago",
            amount = DINNER_AMOUNT,
            personalDelta = -DINNER_DELTA,
            category = ExpenseCategory.FoodAndDrink,
            variant = ExpenseCardVariant.Compact,
        )
        PreviewLabel("Settled")
        DebtshareExpenseCard(
            title = "Dinner at Casa Paco",
            subtitle = "Paid by Maria · 2h ago",
            amount = DINNER_AMOUNT,
            personalDelta = DINNER_DELTA,
            category = ExpenseCategory.FoodAndDrink,
            variant = ExpenseCardVariant.Settled,
        )
        PreviewLabel("Long title + long amount")
        DebtshareExpenseCard(
            title = "Apartment rental with ocean views in Reykjavik",
            subtitle = "Paid by Jorge Sanzo Hernando · 12 minutes ago",
            amount = TAXI_AMOUNT,
            personalDelta = 0.0,
            category = ExpenseCategory.Lodging,
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun ExpenseCardVariantsPreview() {
    ExpenseCardVariantsGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun ExpenseCardVariantsDarkPreview() {
    ExpenseCardVariantsGallery(darkTheme = true)
}
