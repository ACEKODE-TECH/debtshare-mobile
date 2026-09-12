package acekode.debtshare.presentation.home.groups.groupdetail

import acekode.debtshare.presentation.home.groups.MemberBadge
import acekode.debtshare.ui.items.DebtshareButton
import acekode.debtshare.ui.items.DebtshareButtonSize
import acekode.debtshare.ui.items.DebtshareButtonVariant
import acekode.debtshare.ui.items.DebtshareIcon
import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import acekode.debtshare.utils.logDebug
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.arrow_left
import debtshare.app.generated.resources.check
import debtshare.app.generated.resources.cup
import debtshare.app.generated.resources.dots
import debtshare.app.generated.resources.group_detail_add_expense
import debtshare.app.generated.resources.group_detail_balances
import debtshare.app.generated.resources.group_detail_in_favor
import debtshare.app.generated.resources.group_detail_label
import debtshare.app.generated.resources.group_detail_members_since
import debtshare.app.generated.resources.group_detail_settled_badge
import debtshare.app.generated.resources.group_detail_tab_activity
import debtshare.app.generated.resources.group_detail_tab_expenses
import debtshare.app.generated.resources.group_detail_tab_notes
import debtshare.app.generated.resources.group_detail_you_owe
import debtshare.app.generated.resources.group_detail_your_balance
import debtshare.app.generated.resources.plus
import debtshare.app.generated.resources.star
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GroupDetailScreen(
    onBackClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onBalancesClick: () -> Unit,
    onInviteClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    val viewModel = koinViewModel<GroupDetailViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is GroupDetailUiState.Content -> GroupDetailBody(
            uiState = state,
            selectedTab = selectedTab,
            onTabSelected = viewModel::onTabSelected,
            onBackClick = onBackClick,
            onAddExpenseClick = onAddExpenseClick,
            onBalancesClick = onBalancesClick,
            onInviteClick = onInviteClick,
            onMenuClick = onMenuClick,
        )

        is GroupDetailUiState.Loading -> {}

        is GroupDetailUiState.Error -> {}
    }
}

@Composable
private fun GroupDetailBody(
    uiState: GroupDetailUiState.Content,
    selectedTab: GroupDetailTab,
    onTabSelected: (GroupDetailTab) -> Unit,
    onBackClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onBalancesClick: () -> Unit,
    onInviteClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DebtshareTheme.colors.background),
    ) {
        GroupDetailTopBar(
            groupName = uiState.groupName,
            onBackClick = onBackClick,
            onMenuClick = onMenuClick,
        )
        GroupMembersInfo(
            badges = uiState.memberBadges,
            memberCount = uiState.memberCount,
            createdDate = uiState.createdDate,
            onInviteClick = onInviteClick,
        )
        GroupBalanceSection(uiState.balance, uiState.debtSummary)
        GroupActionButtons(
            onAddExpenseClick = onAddExpenseClick,
            onBalancesClick = onBalancesClick,
        )
        GroupTabRow(
            selectedTab = selectedTab,
            expenseCount = uiState.expenseCount,
            onTabSelected = onTabSelected,
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
            uiState.sections.forEach { section ->
                item { ExpenseSectionHeader(section.dateLabel) }
                items(section.items, key = { it.id }) { item ->
                    when (item) {
                        is ExpenseItemUiModel.Expense -> ExpenseItemCard(item)
                        is ExpenseItemUiModel.Settlement -> SettlementItemCard(item)
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun GroupDetailTopBar(
    groupName: String,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        DebtshareIcon(icon = Res.drawable.arrow_left, onClick = onBackClick)

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(Res.string.group_detail_label),
                style = DebtshareTheme.typography.labelUppercase,
                color = DebtshareTheme.colors.textTertiary,
            )
            Text(
                text = groupName,
                style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = DebtshareTheme.colors.textPrimary,
            )
        }

        DebtshareIcon(icon = Res.drawable.dots, onClick = onMenuClick)
    }
}

@Composable
private fun GroupMembersInfo(
    badges: ImmutableList<MemberBadge>,
    memberCount: Int,
    createdDate: String,
    onInviteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DebtshareTheme.colors.card)
            .clickable(onClick = onInviteClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DetailMemberBadgeRow(badges)

        Spacer(Modifier.width(8.dp))

        Text(
            text = stringResource(Res.string.group_detail_members_since, memberCount, createdDate),
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textSecondary,
            modifier = Modifier.weight(1f),
        )

        DebtshareIcon(icon = Res.drawable.plus, tint = DebtshareColors.Neutral.n0)
    }
}

@Composable
private fun DetailMemberBadgeRow(badges: ImmutableList<MemberBadge>) {
    val badgeSize = 28.dp
    val step = 18.dp
    val totalWidth = badgeSize + step * (badges.size - 1)

    Box(
        modifier = Modifier
            .width(totalWidth)
            .height(badgeSize),
    ) {
        badges.forEachIndexed { index, member ->
            Box(
                modifier = Modifier
                    .offset(x = step * index)
                    .zIndex((badges.size - index).toFloat())
                    .size(badgeSize)
                    .background(member.color, CircleShape)
                    .border(1.5.dp, DebtshareTheme.colors.card, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = member.initials,
                    style = DebtshareTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = DebtshareColors.Neutral.n0,
                )
            }
        }
    }
}

@Composable
private fun GroupBalanceSection(
    balance: GroupDetailBalance,
    debtSummary: String,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        Text(
            text = stringResource(Res.string.group_detail_your_balance),
            style = DebtshareTheme.typography.labelUppercase,
            color = DebtshareTheme.colors.textTertiary,
        )

        Spacer(Modifier.height(4.dp))

        BalanceAmount(balance)

        Spacer(Modifier.height(4.dp))

        Text(
            text = debtSummary,
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textTertiary,
        )
    }
}

@Composable
private fun BalanceAmount(balance: GroupDetailBalance) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        when (balance) {
            is GroupDetailBalance.Positive -> {
                Text(
                    text = balance.formattedAmount,
                    style = DebtshareTheme.typography.displayMedium,
                    color = DebtshareColors.Semantic.success,
                )
                Text(
                    text = stringResource(Res.string.group_detail_in_favor),
                    style = DebtshareTheme.typography.bodyMedium,
                    color = DebtshareColors.Semantic.success,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }

            is GroupDetailBalance.Negative -> {
                Text(
                    text = balance.formattedAmount,
                    style = DebtshareTheme.typography.displayMedium,
                    color = DebtshareColors.Semantic.error,
                )
                Text(
                    text = stringResource(Res.string.group_detail_you_owe),
                    style = DebtshareTheme.typography.bodyMedium,
                    color = DebtshareColors.Semantic.error,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }

            is GroupDetailBalance.Settled -> {
                Text(
                    text = "0,00 €",
                    style = DebtshareTheme.typography.displayMedium,
                    color = DebtshareTheme.colors.textTertiary,
                )
            }
        }
    }
}

@Composable
private fun GroupActionButtons(
    onAddExpenseClick: () -> Unit,
    onBalancesClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DebtshareButton(
            text = stringResource(Res.string.group_detail_add_expense),
            onClick = onAddExpenseClick,
            icon = Res.drawable.plus,
            size = DebtshareButtonSize.Large,
            modifier = Modifier.weight(1f),
        )
        DebtshareButton(
            text = stringResource(Res.string.group_detail_balances),
            onClick = onBalancesClick,
            variant = DebtshareButtonVariant.Secondary,
            icon = Res.drawable.star,
            size = DebtshareButtonSize.Large,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun GroupTabRow(
    selectedTab: GroupDetailTab,
    expenseCount: Int,
    onTabSelected: (GroupDetailTab) -> Unit,
) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            GroupTab(
                text = stringResource(Res.string.group_detail_tab_expenses, expenseCount),
                selected = selectedTab == GroupDetailTab.Expenses,
                onClick = { onTabSelected(GroupDetailTab.Expenses) },
            )
            GroupTab(
                text = stringResource(Res.string.group_detail_tab_activity),
                selected = selectedTab == GroupDetailTab.Activity,
                onClick = { onTabSelected(GroupDetailTab.Activity) },
            )
            GroupTab(
                text = stringResource(Res.string.group_detail_tab_notes),
                selected = selectedTab == GroupDetailTab.Notes,
                onClick = { onTabSelected(GroupDetailTab.Notes) },
            )
        }
        HorizontalDivider(
            thickness = 0.5.dp,
            color = DebtshareTheme.colors.border,
        )
    }
}

@Composable
private fun GroupTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = text,
            style = DebtshareTheme.typography.bodyMedium.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            ),
            color = if (selected) {
                DebtshareTheme.colors.textPrimary
            } else {
                DebtshareTheme.colors.textTertiary
            },
        )
    }
}

@Composable
private fun ExpenseSectionHeader(dateLabel: String) {
    Text(
        text = dateLabel,
        style = DebtshareTheme.typography.labelUppercase,
        color = DebtshareTheme.colors.textTertiary,
        modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp),
    )
}

@Composable
private fun ExpenseItemCard(expense: ExpenseItemUiModel.Expense) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(DebtshareTheme.colors.card, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable { logDebug("GroupDetailScreen", "Expense item clicked: ${expense.title}") }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ExpenseIcon(expense.icon, expense.iconTint)

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = expense.title,
                style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = DebtshareTheme.colors.textPrimary,
            )
            Text(
                text = expense.subtitle,
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareTheme.colors.textTertiary,
            )
        }

        Spacer(Modifier.width(8.dp))

        ExpenseAmounts(expense.totalAmount, expense.balanceImpact)
    }
}

@Composable
private fun ExpenseIcon(icon: DrawableResource, tint: Color) {
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
private fun ExpenseAmounts(totalAmount: String, impact: BalanceImpact) {
    Column(horizontalAlignment = Alignment.End) {
        Text(
            text = totalAmount,
            style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = DebtshareTheme.colors.textPrimary,
        )
        val (impactText, impactColor) = when (impact) {
            is BalanceImpact.Positive -> impact.formattedAmount to DebtshareColors.Semantic.success
            is BalanceImpact.Negative -> impact.formattedAmount to DebtshareColors.Semantic.error
        }
        Text(
            text = impactText,
            style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = impactColor,
        )
    }
}

@Composable
private fun SettlementItemCard(settlement: ExpenseItemUiModel.Settlement) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(DebtshareTheme.colors.card, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable { logDebug("GroupDetailScreen", "Settlement item clicked: ${settlement.title}") }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ExpenseIcon(Res.drawable.check, DebtshareColors.Semantic.success)

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = settlement.title,
                style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = DebtshareTheme.colors.textPrimary,
            )
            Text(
                text = settlement.subtitle,
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareTheme.colors.textTertiary,
            )
        }

        Spacer(Modifier.width(8.dp))

        SettlementAmount(settlement.amount)
    }
}

@Composable
private fun SettlementAmount(amount: String) {
    Column(horizontalAlignment = Alignment.End) {
        Text(
            text = amount,
            style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = DebtshareColors.Semantic.success,
        )
        Text(
            text = stringResource(Res.string.group_detail_settled_badge),
            style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = DebtshareColors.Semantic.success,
            modifier = Modifier
                .border(1.dp, DebtshareColors.Semantic.success, RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 1.dp),
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun GroupDetailPreview() {
    DebtshareTheme(darkTheme = false) {
        GroupDetailBody(
            uiState = previewGroupDetail(),
            selectedTab = GroupDetailTab.Expenses,
            onTabSelected = {},
            onBackClick = {},
            onAddExpenseClick = {},
            onBalancesClick = {},
            onInviteClick = {},
            onMenuClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun GroupDetailDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        GroupDetailBody(
            uiState = previewGroupDetail(),
            selectedTab = GroupDetailTab.Expenses,
            onTabSelected = {},
            onBackClick = {},
            onAddExpenseClick = {},
            onBalancesClick = {},
            onInviteClick = {},
            onMenuClick = {},
        )
    }
}

private fun previewGroupDetail() = GroupDetailUiState.Content(
    groupName = "Piso Castellana 43",
    memberCount = 4,
    createdDate = "ene 2025",
    memberBadges = persistentListOf(
        MemberBadge("JS", DebtshareColors.Brand.primary),
        MemberBadge("AM", DebtshareColors.Accent.violet),
        MemberBadge("LP", DebtshareColors.Accent.plum),
        MemberBadge("CR", DebtshareColors.Semantic.success),
    ),
    balance = GroupDetailBalance.Positive("+48,00 €"),
    debtSummary = "Carlos te debe 48,00 €",
    expenseCount = 12,
    sections = persistentListOf(
        ExpenseSection(
            dateLabel = "HOY",
            items = persistentListOf(
                ExpenseItemUiModel.Expense(
                    id = "e1",
                    icon = Res.drawable.cup,
                    iconTint = DebtshareColors.Accent.plum,
                    title = "Cena Casa Lucio",
                    subtitle = "Pagó Ana · reparto entre 4",
                    totalAmount = "96,00 €",
                    balanceImpact = BalanceImpact.Positive("+72,00 €"),
                ),
                ExpenseItemUiModel.Settlement(
                    id = "s1",
                    title = "Luis pagó a Ana",
                    subtitle = "Liquidación · Bizum",
                    amount = "30,00 €",
                ),
            ),
        ),
    ),
)
