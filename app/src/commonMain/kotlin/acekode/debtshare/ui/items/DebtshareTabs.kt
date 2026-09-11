package acekode.debtshare.ui.items

import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareComponentPreview
import acekode.debtshare.ui.utils.PreviewGallery
import acekode.debtshare.ui.utils.PreviewLabel
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

enum class DebtshareTabsVariant { Underline, Pill, Segmented }

@Immutable
data class DebtshareTabItem(
    val label: String,
    val count: Int? = null,
    val enabled: Boolean = true,
)

object DebtshareTabsDefaults {
    val indicatorHeight: Dp = 2.dp
    val borderWidth: Dp = 1.dp
    val segmentedTrackPadding: Dp = 4.dp
    val underlineTopPadding: Dp = 16.dp
    val underlineBottomPadding: Dp = 16.dp
    val segmentedVerticalPadding: Dp = 8.dp
    const val TRANSITION_MILLIS = 150
    const val DISABLED_ALPHA = 0.5f
}

@Composable
fun DebtshareTabs(
    tabs: ImmutableList<DebtshareTabItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    variant: DebtshareTabsVariant = DebtshareTabsVariant.Underline,
) {
    Box(modifier = modifier) {
        when (variant) {
            DebtshareTabsVariant.Underline -> UnderlineTabs(tabs, selectedIndex, onSelect)
            DebtshareTabsVariant.Pill -> PillTabs(tabs, selectedIndex, onSelect)
            DebtshareTabsVariant.Segmented -> SegmentedTabs(tabs, selectedIndex, onSelect)
        }
    }
}

@Composable
private fun UnderlineTabs(
    tabs: List<DebtshareTabItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    val borderColor = DebtshareTheme.colors.neutralTintStrong
    val textPrimary = DebtshareTheme.colors.textPrimary
    val textTertiary = DebtshareTheme.colors.textTertiary
    val strokeWidth = DebtshareTabsDefaults.borderWidth

    ScrollableTabs(
        selectedIndex = selectedIndex,
        tabCount = tabs.size,
        spacing = DebtshareTheme.spacing.large,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.drawBehind {
            val stroke = strokeWidth.toPx()
            val y = size.height - stroke / 2
            drawLine(borderColor, Offset(0f, y), Offset(size.width, y), stroke)
        },
    ) { index, tabModifier ->
        val item = tabs[index]
        val selected = index == selectedIndex
        val indicator by animateColorAsState(
            targetValue = if (selected) DebtshareColors.Brand.primary else Color.Transparent,
            animationSpec = tween(DebtshareTabsDefaults.TRANSITION_MILLIS),
        )
        Column(
            modifier = tabModifier
                .alpha(if (item.enabled) 1f else DebtshareTabsDefaults.DISABLED_ALPHA)
                .clickable(enabled = item.enabled) { onSelect(index) }
                .padding(top = DebtshareTabsDefaults.underlineTopPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TabLabel(
                item = item,
                style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = if (selected) textPrimary else textTertiary,
            )
            Spacer(Modifier.height(DebtshareTabsDefaults.underlineBottomPadding))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(DebtshareTabsDefaults.indicatorHeight)
                    .background(indicator),
            )
        }
    }
}

@Composable
private fun PillTabs(
    tabs: List<DebtshareTabItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    val card = DebtshareTheme.colors.card
    val controlBorder = DebtshareTheme.colors.border
    val textTertiary = DebtshareTheme.colors.textTertiary

    ScrollableTabs(
        selectedIndex = selectedIndex,
        tabCount = tabs.size,
        spacing = DebtshareTheme.spacing.small,
        verticalAlignment = Alignment.CenterVertically,
    ) { index, tabModifier ->
        val item = tabs[index]
        val selected = index == selectedIndex
        val shape = RoundedCornerShape(DebtshareTheme.radius.small)
        Box(
            modifier = tabModifier
                .alpha(if (item.enabled) 1f else DebtshareTabsDefaults.DISABLED_ALPHA)
                .clip(shape)
                .background(if (selected) DebtshareColors.Brand.primaryTint else card)
                .then(
                    if (selected) {
                        Modifier
                    } else {
                        Modifier.border(DebtshareTabsDefaults.borderWidth, controlBorder, shape)
                    },
                )
                .clickable(enabled = item.enabled) { onSelect(index) }
                .padding(horizontal = DebtshareTheme.spacing.medium, vertical = DebtshareTheme.spacing.small),
        ) {
            TabLabel(
                item = item,
                style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = if (selected) DebtshareColors.Brand.primary else textTertiary,
            )
        }
    }
}

@Composable
private fun SegmentedTabs(
    tabs: List<DebtshareTabItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    val segmentedTrack = DebtshareTheme.colors.neutralTint
    val card = DebtshareTheme.colors.card
    val textPrimary = DebtshareTheme.colors.textPrimary
    val textTertiary = DebtshareTheme.colors.textTertiary
    val trackShape = RoundedCornerShape(DebtshareTheme.radius.small)
    val tabShape = RoundedCornerShape(DebtshareTheme.radius.verySmall)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(trackShape)
            .background(segmentedTrack)
            .padding(DebtshareTabsDefaults.segmentedTrackPadding),
        horizontalArrangement = Arrangement.spacedBy(DebtshareTabsDefaults.segmentedTrackPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tabs.forEachIndexed { index, item ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .alpha(if (item.enabled) 1f else DebtshareTabsDefaults.DISABLED_ALPHA)
                    .shadow(
                        elevation = if (selected) DebtshareTheme.elevation.verySmall else DebtshareTheme.elevation.none,
                        shape = tabShape,
                    )
                    .clip(tabShape)
                    .background(if (selected) card else Color.Transparent)
                    .clickable(enabled = item.enabled) { onSelect(index) }
                    .padding(
                        horizontal = DebtshareTheme.spacing.medium,
                        vertical = DebtshareTabsDefaults.segmentedVerticalPadding,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                TabLabel(
                    item = item,
                    style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = if (selected) textPrimary else textTertiary,
                )
            }
        }
    }
}

@Composable
private fun ScrollableTabs(
    selectedIndex: Int,
    tabCount: Int,
    spacing: Dp,
    verticalAlignment: Alignment.Vertical,
    modifier: Modifier = Modifier,
    content: @Composable (Int, Modifier) -> Unit,
) {
    val scrollState = rememberScrollState()
    val bounds = remember { mutableStateMapOf<Int, Pair<Int, Int>>() }
    var viewport by remember { mutableIntStateOf(0) }
    val selectedBounds = bounds[selectedIndex]

    LaunchedEffect(selectedIndex, viewport, selectedBounds) {
        val (start, width) = selectedBounds ?: return@LaunchedEffect
        if (viewport > 0) {
            scrollState.animateScrollTo((start + width / 2 - viewport / 2).coerceAtLeast(0))
        }
    }

    Row(
        modifier = modifier
            .onSizeChanged { viewport = it.width }
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = verticalAlignment,
    ) {
        repeat(tabCount) { index ->
            content(
                index,
                Modifier.onGloballyPositioned { coordinates ->
                    bounds[index] = coordinates.positionInParent().x.toInt() to coordinates.size.width
                },
            )
        }
    }
}

@Composable
private fun TabLabel(
    item: DebtshareTabItem,
    style: TextStyle,
    color: Color,
) {
    val textTertiary = DebtshareTheme.colors.textTertiary
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = item.label, style = style, color = color, maxLines = 1)
        if (item.count != null) {
            Text(
                text = " · ${item.count}",
                style = style,
                color = textTertiary,
                maxLines = 1,
            )
        }
    }
}

private val tabsPreviewNavigationTabs = listOf(
    DebtshareTabItem(label = "Expenses", count = 12),
    DebtshareTabItem(label = "Balances"),
    DebtshareTabItem(label = "Activity", count = 3),
    DebtshareTabItem(label = "Settings", enabled = false),
)

private val tabsPreviewFilterTabs = listOf(
    DebtshareTabItem(label = "All"),
    DebtshareTabItem(label = "Pending", count = 4),
    DebtshareTabItem(label = "Settled"),
)

private val tabsPreviewPeriodTabs = listOf(
    DebtshareTabItem(label = "7d"),
    DebtshareTabItem(label = "30d"),
    DebtshareTabItem(label = "90d"),
)

@Composable
private fun TabsGallery(darkTheme: Boolean, selectedIndex: Int) {
    PreviewGallery(darkTheme = darkTheme) {
        PreviewLabel("Underline")
        DebtshareTabs(
            tabs = tabsPreviewNavigationTabs.toImmutableList(),
            selectedIndex = selectedIndex,
            onSelect = {},
        )
        PreviewLabel("Pill")
        DebtshareTabs(
            tabs = tabsPreviewFilterTabs.toImmutableList(),
            selectedIndex = selectedIndex,
            onSelect = {},
            variant = DebtshareTabsVariant.Pill,
        )
        PreviewLabel("Segmented")
        DebtshareTabs(
            tabs = tabsPreviewPeriodTabs.toImmutableList(),
            selectedIndex = selectedIndex,
            onSelect = {},
            variant = DebtshareTabsVariant.Segmented,
        )
    }
}

@DebtshareComponentPreview
@Composable
private fun DebtshareTabsPreview() {
    TabsGallery(darkTheme = false, selectedIndex = 0)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareTabsDarkPreview() {
    TabsGallery(darkTheme = true, selectedIndex = 0)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareTabsSecondSelectedPreview() {
    TabsGallery(darkTheme = false, selectedIndex = 1)
}

@DebtshareComponentPreview
@Composable
private fun DebtshareTabsSecondSelectedDarkPreview() {
    TabsGallery(darkTheme = true, selectedIndex = 1)
}
