package acekode.debtshare.presentation.home.groups

import acekode.debtshare.ui.items.DebtshareButton
import acekode.debtshare.ui.items.DebtshareButtonSize
import acekode.debtshare.ui.items.DebtshareButtonVariant
import acekode.debtshare.ui.items.DebtshareIcon
import acekode.debtshare.ui.items.DebtshareTabHeader
import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.cup
import debtshare.app.generated.resources.groups_balance_across
import debtshare.app.generated.resources.groups_balance_owed
import debtshare.app.generated.resources.groups_balance_owing
import debtshare.app.generated.resources.groups_balance_settled
import debtshare.app.generated.resources.groups_balance_settled_label
import debtshare.app.generated.resources.groups_create_group
import debtshare.app.generated.resources.groups_empty_description
import debtshare.app.generated.resources.groups_empty_title
import debtshare.app.generated.resources.groups_filter_all
import debtshare.app.generated.resources.groups_filter_archived
import debtshare.app.generated.resources.groups_filter_with_activity
import debtshare.app.generated.resources.groups_global_balance
import debtshare.app.generated.resources.groups_join_with_code
import debtshare.app.generated.resources.groups_members_expenses
import debtshare.app.generated.resources.groups_subtitle_content
import debtshare.app.generated.resources.groups_subtitle_empty
import debtshare.app.generated.resources.house
import debtshare.app.generated.resources.inbox
import debtshare.app.generated.resources.plus
import debtshare.app.generated.resources.search
import debtshare.app.generated.resources.star
import debtshare.app.generated.resources.tab_home
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GroupsScreen(
    onCreateGroupClick: () -> Unit,
    onJoinWithCodeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onGroupClick: (String) -> Unit,
) {
    val viewModel = koinViewModel<GroupsViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()

    when (uiState) {
        is GroupsUiState.Empty -> GroupsEmptyContent(
            onCreateGroupClick = onCreateGroupClick,
            onJoinWithCodeClick = onJoinWithCodeClick,
        )

        is GroupsUiState.Content -> GroupsListContent(
            uiState = uiState as GroupsUiState.Content,
            selectedFilter = selectedFilter,
            onFilterSelected = viewModel::onFilterSelected,
            onSearchClick = onSearchClick,
            onCreateGroupClick = onCreateGroupClick,
            onGroupClick = onGroupClick,
        )

        is GroupsUiState.Loading -> {}

        is GroupsUiState.Error -> {}
    }
}

@Composable
private fun GroupsEmptyContent(
    onCreateGroupClick: () -> Unit,
    onJoinWithCodeClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DebtshareTheme.colors.background)
            .padding(horizontal = 16.dp),
    ) {
        Spacer(Modifier.height(16.dp))

        GroupsEmptyHeader()

        GroupsEmptyBody(
            onCreateGroupClick = onCreateGroupClick,
            onJoinWithCodeClick = onJoinWithCodeClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun GroupsEmptyHeader() {
    DebtshareTabHeader(
        title = stringResource(Res.string.tab_home),
        subtitle = stringResource(Res.string.groups_subtitle_empty),
    )
}

@Composable
private fun GroupsEmptyBody(
    onCreateGroupClick: () -> Unit,
    onJoinWithCodeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(Res.string.groups_empty_title),
            style = DebtshareTheme.typography.displaySmall,
            color = DebtshareTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.groups_empty_description),
            style = DebtshareTheme.typography.bodyMedium,
            color = DebtshareTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(32.dp))

        DebtshareButton(
            text = stringResource(Res.string.groups_create_group),
            onClick = onCreateGroupClick,
            icon = Res.drawable.plus,
            size = DebtshareButtonSize.Large,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(12.dp))

        DebtshareButton(
            text = stringResource(Res.string.groups_join_with_code),
            onClick = onJoinWithCodeClick,
            variant = DebtshareButtonVariant.Secondary,
            icon = Res.drawable.inbox,
            size = DebtshareButtonSize.Large,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun GroupsListContent(
    uiState: GroupsUiState.Content,
    selectedFilter: GroupFilter,
    onFilterSelected: (GroupFilter) -> Unit,
    onSearchClick: () -> Unit,
    onCreateGroupClick: () -> Unit,
    onGroupClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DebtshareTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Spacer(Modifier.height(16.dp))

        GroupsContentHeader(
            activeCount = uiState.activeGroupCount,
            globalBalance = uiState.globalBalance,
            onSearchClick = onSearchClick,
            onCreateGroupClick = onCreateGroupClick,
        )

        Spacer(Modifier.height(16.dp))

        GroupsFilterRow(
            selectedFilter = selectedFilter,
            groupCount = uiState.groups.size,
            onFilterSelected = onFilterSelected,
        )

        Spacer(Modifier.height(16.dp))

        GlobalBalanceCard(
            balance = uiState.globalBalance,
            groupCount = uiState.groups.size,
        )

        Spacer(Modifier.height(16.dp))

        GroupList(
            groups = uiState.groups,
            onGroupClick = onGroupClick,
        )

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun GroupsContentHeader(
    activeCount: Int,
    globalBalance: String,
    onSearchClick: () -> Unit,
    onCreateGroupClick: () -> Unit,
) {
    DebtshareTabHeader(
        title = stringResource(Res.string.tab_home),
        subtitle = stringResource(Res.string.groups_subtitle_content, activeCount, globalBalance),
    ) {
        DebtshareIcon(icon = Res.drawable.search, onClick = onSearchClick)

        Spacer(Modifier.width(16.dp))

        DebtshareIcon(icon = Res.drawable.plus, onClick = onCreateGroupClick)
    }
}

@Composable
private fun GroupsFilterRow(
    selectedFilter: GroupFilter,
    groupCount: Int,
    onFilterSelected: (GroupFilter) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        GroupFilterChip(
            text = stringResource(Res.string.groups_filter_all, groupCount),
            selected = selectedFilter == GroupFilter.All,
            onClick = { onFilterSelected(GroupFilter.All) },
        )
        GroupFilterChip(
            text = stringResource(Res.string.groups_filter_with_activity),
            selected = selectedFilter == GroupFilter.WithActivity,
            onClick = { onFilterSelected(GroupFilter.WithActivity) },
        )
        GroupFilterChip(
            text = stringResource(Res.string.groups_filter_archived),
            selected = selectedFilter == GroupFilter.Archived,
            onClick = { onFilterSelected(GroupFilter.Archived) },
        )
    }
}

@Composable
private fun GroupFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = if (selected) {
        DebtshareTheme.colors.textPrimary
    } else {
        DebtshareTheme.colors.card
    }
    val textColor = if (selected) {
        DebtshareTheme.colors.background
    } else {
        DebtshareTheme.colors.textPrimary
    }
    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier
            .clip(shape)
            .then(
                if (selected) Modifier else Modifier.border(1.dp, DebtshareTheme.colors.border, shape),
            )
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = text,
            style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = textColor,
        )
    }
}

@Composable
private fun GlobalBalanceCard(
    balance: String,
    groupCount: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DebtshareColors.Brand.primary, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Column {
            Text(
                text = stringResource(Res.string.groups_global_balance),
                style = DebtshareTheme.typography.labelUppercase,
                color = DebtshareColors.Neutral.n0.copy(alpha = 0.7f),
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = balance,
                style = DebtshareTheme.typography.displayMedium,
                color = DebtshareColors.Neutral.n0,
            )
        }

        Text(
            text = stringResource(Res.string.groups_balance_across, groupCount),
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareColors.Neutral.n0.copy(alpha = 0.7f),
            textAlign = TextAlign.End,
        )
    }
}

@Composable
private fun GroupList(
    groups: ImmutableList<GroupSummaryUiModel>,
    onGroupClick: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        groups.forEach { group ->
            GroupItem(
                group = group,
                onClick = { onGroupClick(group.id) },
            )
        }
    }
}

@Composable
private fun GroupItem(
    group: GroupSummaryUiModel,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DebtshareTheme.colors.card, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GroupItemIcon(group.icon, group.iconTint)

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            GroupItemTitle(group.name, group.hasActivity)

            Spacer(Modifier.height(4.dp))

            GroupItemSubtitle(group.members, group.memberCount, group.expenseCount)
        }

        Spacer(Modifier.width(8.dp))

        GroupItemBalance(group.balance)
    }
}

@Composable
private fun GroupItemIcon(
    icon: DrawableResource,
    tint: Color,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(tint.copy(alpha = 0.12f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        DebtshareIcon(icon = icon, tint = tint)
    }
}

@Composable
private fun GroupItemTitle(name: String, hasActivity: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = name,
            style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = DebtshareTheme.colors.textPrimary,
        )

        if (hasActivity) {
            Spacer(Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(DebtshareColors.Brand.primary, CircleShape),
            )
        }
    }
}

@Composable
private fun GroupItemSubtitle(
    members: ImmutableList<MemberBadge>,
    memberCount: Int,
    expenseCount: Int,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        MemberBadgeRow(members = members, totalCount = memberCount)

        Spacer(Modifier.width(6.dp))

        Text(
            text = stringResource(Res.string.groups_members_expenses, memberCount, expenseCount),
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textTertiary,
        )
    }
}

@Composable
private fun MemberBadgeRow(
    members: ImmutableList<MemberBadge>,
    totalCount: Int,
) {
    val maxVisible = 3
    val visible = members.take(maxVisible)
    val remaining = totalCount - maxVisible
    val badgeSize = 24.dp
    val step = 16.dp
    val extraBadge = if (remaining > 0) 1 else 0
    val totalWidth = badgeSize + step * (visible.size + extraBadge - 1)

    Box(
        modifier = Modifier
            .width(totalWidth)
            .height(badgeSize),
    ) {
        visible.forEachIndexed { index, member ->
            BadgeCircle(
                text = member.initials,
                backgroundColor = member.color,
                textColor = DebtshareColors.Neutral.n0,
                modifier = Modifier
                    .offset(x = step * index)
                    .zIndex((visible.size - index).toFloat()),
            )
        }

        if (remaining > 0) {
            BadgeCircle(
                text = "+$remaining",
                backgroundColor = DebtshareTheme.colors.neutralTintStrong,
                textColor = DebtshareTheme.colors.textSecondary,
                modifier = Modifier
                    .offset(x = step * visible.size)
                    .zIndex(0f),
            )
        }
    }
}

@Composable
private fun BadgeCircle(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .background(backgroundColor, CircleShape)
            .border(1.5.dp, DebtshareTheme.colors.card, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DebtshareTheme.typography.bodySmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = textColor,
        )
    }
}

@Composable
private fun GroupItemBalance(balance: GroupBalance) {
    Column(horizontalAlignment = Alignment.End) {
        when (balance) {
            is GroupBalance.Positive -> {
                Text(
                    text = balance.formattedAmount,
                    style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = DebtshareColors.Semantic.success,
                )
                Text(
                    text = stringResource(Res.string.groups_balance_owed),
                    style = DebtshareTheme.typography.bodySmall,
                    color = DebtshareTheme.colors.textTertiary,
                )
            }

            is GroupBalance.Negative -> {
                Text(
                    text = balance.formattedAmount,
                    style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = DebtshareColors.Semantic.error,
                )
                Text(
                    text = stringResource(Res.string.groups_balance_owing),
                    style = DebtshareTheme.typography.bodySmall,
                    color = DebtshareTheme.colors.textTertiary,
                )
            }

            is GroupBalance.Settled -> {
                Text(
                    text = stringResource(Res.string.groups_balance_settled),
                    style = DebtshareTheme.typography.bodyMedium,
                    color = DebtshareTheme.colors.textTertiary,
                )
                Text(
                    text = stringResource(Res.string.groups_balance_settled_label),
                    style = DebtshareTheme.typography.bodySmall,
                    color = DebtshareTheme.colors.textTertiary,
                )
            }
        }
    }
}

@DebtshareScreenPreview
@Composable
private fun GroupsEmptyPreview() {
    DebtshareTheme(darkTheme = false) {
        GroupsEmptyContent(
            onCreateGroupClick = {},
            onJoinWithCodeClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun GroupsEmptyDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        GroupsEmptyContent(
            onCreateGroupClick = {},
            onJoinWithCodeClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun GroupsContentPreview() {
    DebtshareTheme(darkTheme = false) {
        GroupsListContent(
            uiState = previewContent(),
            selectedFilter = GroupFilter.All,
            onFilterSelected = {},
            onSearchClick = {},
            onCreateGroupClick = {},
            onGroupClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun GroupsContentDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        GroupsListContent(
            uiState = previewContent(),
            selectedFilter = GroupFilter.All,
            onFilterSelected = {},
            onSearchClick = {},
            onCreateGroupClick = {},
            onGroupClick = {},
        )
    }
}

private fun previewContent() = GroupsUiState.Content(
    globalBalance = "+3,80 €",
    activeGroupCount = 4,
    groups = persistentListOf(
        GroupSummaryUiModel(
            id = "1",
            name = "Piso Castellana 43",
            icon = Res.drawable.house,
            iconTint = DebtshareColors.Brand.primary,
            memberCount = 4,
            expenseCount = 14,
            balance = GroupBalance.Positive("+3,80 €"),
            hasActivity = true,
            members = persistentListOf(
                MemberBadge("JS", DebtshareColors.Brand.primary),
                MemberBadge("AM", DebtshareColors.Accent.violet),
                MemberBadge("LP", DebtshareColors.Accent.plum),
            ),
        ),
        GroupSummaryUiModel(
            id = "2",
            name = "Viaje Lisboa",
            icon = Res.drawable.star,
            iconTint = DebtshareColors.Accent.mustardDark,
            memberCount = 6,
            expenseCount = 28,
            balance = GroupBalance.Negative("-24,50 €"),
            hasActivity = false,
            members = persistentListOf(
                MemberBadge("JS", DebtshareColors.Brand.primary),
                MemberBadge("CR", DebtshareColors.Semantic.success),
                MemberBadge("MT", DebtshareColors.Accent.plumBright),
            ),
        ),
        GroupSummaryUiModel(
            id = "3",
            name = "Cena Nochevieja",
            icon = Res.drawable.cup,
            iconTint = DebtshareColors.Accent.plum,
            memberCount = 8,
            expenseCount = 12,
            balance = GroupBalance.Settled,
            hasActivity = false,
            members = persistentListOf(
                MemberBadge("PG", DebtshareColors.Accent.mustard),
            ),
        ),
    ),
)
