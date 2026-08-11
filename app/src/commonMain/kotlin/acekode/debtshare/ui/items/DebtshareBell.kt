package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import acekode.debtshare.ui.utils.PreviewGallery
import acekode.debtshare.ui.utils.PreviewLabel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.bell
import org.jetbrains.compose.resources.painterResource

object DebtshareBellDefaults {
    val buttonSize: Dp = 32.dp
    val iconSize: Dp = 16.dp
    val badgeSize: Dp = 16.dp
    val dotSize: Dp = 8.dp
    val badgeOffsetX: Dp = 4.dp
    val badgeOffsetY: Dp = (-4).dp
    val badgeCutOut: Dp = 2.dp
    val badgeHorizontalPadding: Dp = 4.dp
    val borderWidth: Dp = 1.dp
    val focusRingWidth: Dp = 2.dp
    const val MAX_COUNT = 99
    const val ENTER_MILLIS = 200
    const val EXIT_MILLIS = 150
    const val ENTER_SCALE = 0.5f
}

@Composable
fun DebtshareBell(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    count: Int = 0,
    dotOnly: Boolean = false,
    expanded: Boolean = false,
    enabled: Boolean = true,
) {
    Box(modifier = modifier) {
        BellButton(onClick = onClick, expanded = expanded, enabled = enabled)
        BellBadge(
            count = count,
            dotOnly = dotOnly,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(
                    x = DebtshareBellDefaults.badgeOffsetX,
                    y = DebtshareBellDefaults.badgeOffsetY,
                ),
        )
    }
}

@Composable
private fun BellButton(
    onClick: () -> Unit,
    expanded: Boolean,
    enabled: Boolean,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val shape = RoundedCornerShape(DebtshareTheme.radius.medium)

    Box(
        modifier = Modifier
            .size(DebtshareBellDefaults.buttonSize)
            .clip(shape)
            .background(if (expanded) DebtshareColors.Brand.primaryTint else DebtshareTheme.colors.card)
            .border(
                width = if (focused) {
                    DebtshareBellDefaults.focusRingWidth
                } else {
                    DebtshareBellDefaults.borderWidth
                },
                color = if (focused) DebtshareColors.Brand.primary else DebtshareTheme.colors.border,
                shape = shape,
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.bell),
            contentDescription = null,
            tint = if (expanded) DebtshareColors.Brand.primary else DebtshareTheme.colors.textSecondary,
            modifier = Modifier.size(DebtshareBellDefaults.iconSize),
        )
    }
}

@Composable
private fun BellBadge(
    count: Int,
    dotOnly: Boolean,
    modifier: Modifier = Modifier,
) {
    val bump = remember { Animatable(1f) }
    var previous by remember { mutableIntStateOf(count) }

    LaunchedEffect(count) {
        if (previous in 1..<count) {
            bump.snapTo(DebtshareBellDefaults.ENTER_SCALE)
            bump.animateTo(1f, tween(DebtshareBellDefaults.ENTER_MILLIS))
        }
        previous = count
    }

    AnimatedVisibility(
        visible = count > 0,
        enter = scaleIn(
            initialScale = DebtshareBellDefaults.ENTER_SCALE,
            animationSpec = tween(DebtshareBellDefaults.ENTER_MILLIS),
        ),
        exit = fadeOut(tween(DebtshareBellDefaults.EXIT_MILLIS)),
        modifier = modifier,
    ) {
        BellBadgeContent(count = count, dotOnly = dotOnly, scale = bump.value)
    }
}

@Composable
private fun BellBadgeContent(count: Int, dotOnly: Boolean, scale: Float) {
    val card = DebtshareTheme.colors.card
    if (dotOnly) {
        Box(
            modifier = Modifier
                .scale(scale)
                .size(DebtshareBellDefaults.dotSize)
                .border(DebtshareBellDefaults.badgeCutOut, card, CircleShape)
                .clip(CircleShape)
                .background(DebtshareColors.Semantic.error),
        )
    } else {
        Box(
            modifier = Modifier
                .scale(scale)
                .defaultMinSize(
                    minWidth = DebtshareBellDefaults.badgeSize,
                    minHeight = DebtshareBellDefaults.badgeSize,
                )
                .border(DebtshareBellDefaults.badgeCutOut, card, CircleShape)
                .clip(CircleShape)
                .background(DebtshareColors.Semantic.error)
                .padding(horizontal = DebtshareBellDefaults.badgeHorizontalPadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (count > DebtshareBellDefaults.MAX_COUNT) {
                    "${DebtshareBellDefaults.MAX_COUNT}+"
                } else {
                    count.toString()
                },
                style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.ExtraBold),
                color = DebtshareColors.Neutral.n0,
                maxLines = 1,
            )
        }
    }
}

private val bellPreviewCounts = listOf(0, 1, 5, 42, 99, 150)

@Composable
private fun BellGallery(darkTheme: Boolean) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Conteo · 0 · 1 · 5 · 42 · 99 · 150")
        Row(
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            bellPreviewCounts.forEach { count ->
                DebtshareBell(onClick = {}, count = count)
            }
        }
        PreviewLabel("Solo punto")
        Row(
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareBell(onClick = {}, count = 0, dotOnly = true)
            DebtshareBell(onClick = {}, count = 7, dotOnly = true)
        }
        PreviewLabel("Open / disabled")
        Row(
            horizontalArrangement = Arrangement.spacedBy(DebtshareTheme.spacing.large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DebtshareBell(onClick = {}, count = 3, expanded = true)
            DebtshareBell(onClick = {}, count = 3, enabled = false)
        }
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareBellPreview() {
    BellGallery(darkTheme = false)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareBellDarkPreview() {
    BellGallery(darkTheme = true)
}
