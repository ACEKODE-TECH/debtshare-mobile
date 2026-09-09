package acekode.debtshare.presentation.home.profile

import acekode.debtshare.ui.items.DebtshareAvatar
import acekode.debtshare.ui.items.DebtshareAvatarSize
import acekode.debtshare.ui.items.DebtshareButtonSize
import acekode.debtshare.ui.items.DebtshareIconButton
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.alert
import debtshare.app.generated.resources.bag
import debtshare.app.generated.resources.bell
import debtshare.app.generated.resources.car
import debtshare.app.generated.resources.chevron_down
import debtshare.app.generated.resources.chevron_right
import debtshare.app.generated.resources.credit_card
import debtshare.app.generated.resources.cup
import debtshare.app.generated.resources.dots
import debtshare.app.generated.resources.groups_balance_settled_label
import debtshare.app.generated.resources.groups_members_expenses
import debtshare.app.generated.resources.house
import debtshare.app.generated.resources.logout
import debtshare.app.generated.resources.mail
import debtshare.app.generated.resources.profile_account
import debtshare.app.generated.resources.profile_all_groups
import debtshare.app.generated.resources.profile_analytics
import debtshare.app.generated.resources.profile_analytics_subtitle
import debtshare.app.generated.resources.profile_average_ticket
import debtshare.app.generated.resources.profile_balance_in_favor
import debtshare.app.generated.resources.profile_balance_you_owe
import debtshare.app.generated.resources.profile_by_category
import debtshare.app.generated.resources.profile_category_subtitle
import debtshare.app.generated.resources.profile_email_password
import debtshare.app.generated.resources.profile_expenses_count
import debtshare.app.generated.resources.profile_global_balance
import debtshare.app.generated.resources.profile_group_comparison
import debtshare.app.generated.resources.profile_group_comparison_subtitle
import debtshare.app.generated.resources.profile_last_90_days
import debtshare.app.generated.resources.profile_logout
import debtshare.app.generated.resources.profile_notifications
import debtshare.app.generated.resources.profile_owed_to_you
import debtshare.app.generated.resources.profile_payment_method
import debtshare.app.generated.resources.profile_this_month
import debtshare.app.generated.resources.profile_tip
import debtshare.app.generated.resources.profile_total
import debtshare.app.generated.resources.profile_total_spent
import debtshare.app.generated.resources.profile_user_expenses
import debtshare.app.generated.resources.profile_user_groups
import debtshare.app.generated.resources.profile_you_owe_label
import debtshare.app.generated.resources.settings
import debtshare.app.generated.resources.star
import debtshare.app.generated.resources.tab_profile
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is ProfileUiState.Loading -> {}

        is ProfileUiState.Error -> {}

        is ProfileUiState.Content -> ProfileContent(
            uiState = uiState as ProfileUiState.Content,
            onSettingsClick = onSettingsClick,
            onLogoutClick = onLogoutClick,
        )
    }
}

@Composable
private fun ProfileContent(
    uiState: ProfileUiState.Content,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DebtshareTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Spacer(Modifier.height(16.dp))
        ProfileHeader(onSettingsClick = onSettingsClick)
        Spacer(Modifier.height(16.dp))
        UserCard(user = uiState.user)
        Spacer(Modifier.height(12.dp))
        BalanceCard(balance = uiState.balance)
        Spacer(Modifier.height(24.dp))
        AnalyticsHeader(groupCount = uiState.analytics.groupCount)
        Spacer(Modifier.height(12.dp))
        StatCards(analytics = uiState.analytics)
        Spacer(Modifier.height(24.dp))
        CategoriesSection(
            categories = uiState.categories,
            totalAmount = uiState.analytics.totalSpent,
            groupCount = uiState.analytics.groupCount,
        )
        Spacer(Modifier.height(24.dp))
        GroupComparisonSection(groups = uiState.groupComparisons)
        Spacer(Modifier.height(12.dp))
        TipText()
        Spacer(Modifier.height(24.dp))
        AccountSection(
            paymentMethod = uiState.account.paymentMethod,
            onLogoutClick = onLogoutClick,
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ProfileHeader(onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.tab_profile),
            style = DebtshareTheme.typography.displayMedium,
            color = DebtshareTheme.colors.textPrimary,
        )
        DebtshareIconButton(
            icon = Res.drawable.settings,
            contentDescription = null,
            onClick = onSettingsClick,
            size = DebtshareButtonSize.Large,
        )
    }
}

@Composable
private fun UserCard(user: ProfileUser) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DebtshareTheme.colors.card, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DebtshareAvatar(
            name = user.name,
            size = DebtshareAvatarSize.Large,
        )

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = user.name,
                style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = DebtshareTheme.colors.textPrimary,
            )
            Text(
                text = "@${user.alias}",
                style = DebtshareTheme.typography.bodyMedium,
                color = DebtshareTheme.colors.textTertiary,
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                UserStatBadge(stringResource(Res.string.profile_user_groups, user.groupCount))
                UserStatBadge(stringResource(Res.string.profile_user_expenses, user.expenseCount))
            }
        }
    }
}

@Composable
private fun UserStatBadge(text: String) {
    Text(
        text = text,
        style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
        color = DebtshareColors.Brand.primary,
        modifier = Modifier
            .background(DebtshareColors.Brand.primaryTint, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    )
}

@Composable
private fun BalanceCard(balance: ProfileBalance) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DebtshareTheme.colors.card, RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(Res.string.profile_global_balance),
            style = DebtshareTheme.typography.labelUppercase,
            color = DebtshareTheme.colors.textTertiary,
        )

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = balance.total,
                style = DebtshareTheme.typography.displayMedium,
                color = DebtshareTheme.colors.textPrimary,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (balance.isPositive) {
                    stringResource(Res.string.profile_balance_in_favor)
                } else {
                    stringResource(Res.string.profile_balance_you_owe)
                },
                style = DebtshareTheme.typography.bodyMedium,
                color = if (balance.isPositive) {
                    DebtshareColors.Semantic.success
                } else {
                    DebtshareColors.Semantic.error
                },
            )
        }

        Spacer(Modifier.height(12.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(DebtshareTheme.colors.border),
        )

        Spacer(Modifier.height(12.dp))

        BalanceSubItems(balance = balance)
    }
}

@Composable
private fun BalanceSubItems(balance: ProfileBalance) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        BalanceSubItem(
            label = stringResource(Res.string.profile_owed_to_you),
            value = balance.owed,
            valueColor = DebtshareColors.Semantic.success,
        )
        BalanceSubItem(
            label = stringResource(Res.string.profile_you_owe_label),
            value = balance.owing,
            valueColor = DebtshareColors.Semantic.error,
        )
        BalanceSubItem(
            label = stringResource(Res.string.profile_this_month),
            value = balance.thisMonth,
            valueColor = DebtshareTheme.colors.textPrimary,
        )
    }
}

@Composable
private fun BalanceSubItem(
    label: String,
    value: String,
    valueColor: Color,
) {
    Column {
        Text(
            text = label,
            style = DebtshareTheme.typography.labelUppercase,
            color = DebtshareTheme.colors.textTertiary,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = valueColor,
        )
    }
}

@Composable
private fun AnalyticsHeader(groupCount: Int) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.profile_analytics),
                style = DebtshareTheme.typography.displaySmall,
                color = DebtshareTheme.colors.textPrimary,
            )
            DropdownChip(text = stringResource(Res.string.profile_last_90_days))
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = stringResource(Res.string.profile_analytics_subtitle, groupCount),
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textTertiary,
        )
    }
}

@Composable
private fun DropdownChip(text: String) {
    Row(
        modifier = Modifier
            .border(1.dp, DebtshareTheme.colors.border, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = text,
            style = DebtshareTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = DebtshareTheme.colors.textPrimary,
        )
        Icon(
            painter = painterResource(Res.drawable.chevron_down),
            contentDescription = null,
            tint = DebtshareTheme.colors.textTertiary,
            modifier = Modifier.size(14.dp),
        )
    }
}

@Composable
private fun StatCards(analytics: ProfileAnalytics) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatCard(
            label = stringResource(Res.string.profile_total_spent),
            value = analytics.totalSpent,
            subtitle = analytics.spentDelta,
            subtitleColor = if (analytics.isDeltaPositive) {
                DebtshareColors.Semantic.success
            } else {
                DebtshareColors.Semantic.error
            },
            modifier = Modifier.weight(1f),
        )
        StatCard(
            label = stringResource(Res.string.profile_average_ticket),
            value = analytics.averageTicket,
            subtitle = stringResource(Res.string.profile_expenses_count, analytics.expenseCount),
            subtitleColor = null,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    subtitle: String?,
    subtitleColor: Color?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(DebtshareTheme.colors.card, RoundedCornerShape(12.dp))
            .padding(12.dp),
    ) {
        Text(
            text = label,
            style = DebtshareTheme.typography.labelUppercase,
            color = DebtshareTheme.colors.textTertiary,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            style = DebtshareTheme.typography.displaySmall,
            color = DebtshareTheme.colors.textPrimary,
        )
        if (subtitle != null) {
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = DebtshareTheme.typography.bodySmall,
                color = subtitleColor ?: DebtshareTheme.colors.textTertiary,
            )
        }
    }
}

@Composable
private fun CategoriesSection(
    categories: ImmutableList<ProfileCategory>,
    totalAmount: String,
    groupCount: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DebtshareTheme.colors.card, RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        CategoriesHeader(totalAmount = totalAmount, groupCount = groupCount)

        Spacer(Modifier.height(16.dp))

        categories.forEachIndexed { index, category ->
            CategoryItem(category = category)
            if (index < categories.lastIndex) {
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CategoriesHeader(totalAmount: String, groupCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.profile_by_category),
            style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = DebtshareTheme.colors.textPrimary,
        )
        DropdownChip(text = stringResource(Res.string.profile_all_groups))
    }

    Spacer(Modifier.height(4.dp))

    Text(
        text = stringResource(Res.string.profile_category_subtitle, totalAmount, groupCount),
        style = DebtshareTheme.typography.bodySmall,
        color = DebtshareTheme.colors.textTertiary,
    )
}

@Composable
private fun CategoryItem(category: ProfileCategory) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CategoryIcon(icon = category.icon, tint = category.iconTint)

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            CategoryItemHeader(category = category)
            Spacer(Modifier.height(6.dp))
            ProgressBar(progress = category.barProgress, color = category.barColor)
        }
    }
}

@Composable
private fun CategoryIcon(icon: DrawableResource, tint: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(tint.copy(alpha = 0.12f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun CategoryItemHeader(category: ProfileCategory) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = category.name,
            style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = DebtshareTheme.colors.textPrimary,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = category.amount,
                style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = DebtshareTheme.colors.textPrimary,
            )
            Text(
                text = "${category.percentage}%",
                style = DebtshareTheme.typography.bodyMedium,
                color = DebtshareTheme.colors.textTertiary,
            )
        }
    }
}

@Composable
private fun ProgressBar(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(DebtshareTheme.colors.neutralTint, RoundedCornerShape(2.dp)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                .height(4.dp)
                .background(color, RoundedCornerShape(2.dp)),
        )
    }
}

@Composable
private fun GroupComparisonSection(groups: ImmutableList<ProfileGroupComparison>) {
    Column {
        GroupComparisonHeader()

        Spacer(Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            groups.forEach { group ->
                GroupComparisonItem(group = group)
            }
        }
    }
}

@Composable
private fun GroupComparisonHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.profile_group_comparison),
            style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = DebtshareTheme.colors.textPrimary,
        )
        DropdownChip(text = stringResource(Res.string.profile_total))
    }

    Spacer(Modifier.height(4.dp))

    Text(
        text = stringResource(Res.string.profile_group_comparison_subtitle),
        style = DebtshareTheme.typography.bodySmall,
        color = DebtshareTheme.colors.textTertiary,
    )
}

@Composable
private fun GroupComparisonItem(group: ProfileGroupComparison) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            CategoryIcon(icon = group.icon, tint = group.iconTint)

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.name,
                    style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = DebtshareTheme.colors.textPrimary,
                )
                Text(
                    text = stringResource(
                        Res.string.groups_members_expenses,
                        group.memberCount,
                        group.expenseCount,
                    ),
                    style = DebtshareTheme.typography.bodySmall,
                    color = DebtshareTheme.colors.textTertiary,
                )
            }

            Spacer(Modifier.width(8.dp))

            GroupComparisonAmounts(group = group)
        }

        Spacer(Modifier.height(8.dp))

        ProgressBar(
            progress = group.barProgress,
            color = group.barColor,
            modifier = Modifier.padding(start = 52.dp),
        )
    }
}

@Composable
private fun GroupComparisonAmounts(group: ProfileGroupComparison) {
    Column(horizontalAlignment = Alignment.End) {
        Text(
            text = group.totalAmount,
            style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = DebtshareTheme.colors.textPrimary,
        )
        when (group.balance) {
            is ProfileGroupBalance.Positive -> Text(
                text = "${group.balance.formattedAmount} ${stringResource(Res.string.profile_balance_in_favor)}",
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareColors.Semantic.success,
            )

            is ProfileGroupBalance.Negative -> Text(
                text = "${group.balance.formattedAmount} ${stringResource(Res.string.profile_balance_you_owe)}",
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareColors.Semantic.error,
            )

            is ProfileGroupBalance.Settled -> Text(
                text = stringResource(Res.string.groups_balance_settled_label),
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareTheme.colors.textTertiary,
            )
        }
    }
}

@Composable
private fun TipText() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(Res.drawable.alert),
            contentDescription = null,
            tint = DebtshareTheme.colors.textTertiary,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = stringResource(Res.string.profile_tip),
            style = DebtshareTheme.typography.bodySmall,
            color = DebtshareTheme.colors.textTertiary,
        )
    }
}

@Composable
private fun AccountSection(
    paymentMethod: String?,
    onLogoutClick: () -> Unit,
) {
    Column {
        Text(
            text = stringResource(Res.string.profile_account),
            style = DebtshareTheme.typography.labelUppercase,
            color = DebtshareTheme.colors.textTertiary,
        )

        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DebtshareTheme.colors.card, RoundedCornerShape(16.dp)),
        ) {
            AccountItem(
                icon = Res.drawable.mail,
                text = stringResource(Res.string.profile_email_password),
                onClick = {},
            )
            AccountDivider()
            AccountItem(
                icon = Res.drawable.bell,
                text = stringResource(Res.string.profile_notifications),
                onClick = {},
            )
            AccountDivider()
            AccountItem(
                icon = Res.drawable.credit_card,
                text = stringResource(Res.string.profile_payment_method),
                trailingText = paymentMethod,
                onClick = {},
            )
            AccountDivider()
            LogoutItem(onClick = onLogoutClick)
        }
    }
}

@Composable
private fun AccountItem(
    icon: DrawableResource,
    text: String,
    onClick: () -> Unit,
    trailingText: String? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = DebtshareTheme.colors.textTertiary,
            modifier = Modifier.size(20.dp),
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = text,
            style = DebtshareTheme.typography.bodyLarge,
            color = DebtshareTheme.colors.textPrimary,
            modifier = Modifier.weight(1f),
        )

        if (trailingText != null) {
            Text(
                text = trailingText,
                style = DebtshareTheme.typography.bodyMedium,
                color = DebtshareTheme.colors.textTertiary,
            )
            Spacer(Modifier.width(8.dp))
        }

        Icon(
            painter = painterResource(Res.drawable.chevron_right),
            contentDescription = null,
            tint = DebtshareTheme.colors.textMuted,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun AccountDivider() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(DebtshareTheme.colors.border),
    )
}

@Composable
private fun LogoutItem(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.logout),
            contentDescription = null,
            tint = DebtshareColors.Semantic.error,
            modifier = Modifier.size(20.dp),
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = stringResource(Res.string.profile_logout),
            style = DebtshareTheme.typography.bodyLarge,
            color = DebtshareColors.Semantic.error,
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun ProfileContentPreview() {
    DebtshareTheme(darkTheme = false) {
        ProfileContent(
            uiState = previewContent(),
            onSettingsClick = {},
            onLogoutClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun ProfileContentDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        ProfileContent(
            uiState = previewContent(),
            onSettingsClick = {},
            onLogoutClick = {},
        )
    }
}

private fun previewContent() = ProfileUiState.Content(
    user = ProfileUser(
        name = "Ana García",
        alias = "anag",
        groupCount = 5,
        expenseCount = 32,
    ),
    balance = ProfileBalance(
        total = "+48,00 €",
        isPositive = true,
        owed = "+72,50 €",
        owing = "−24,50 €",
        thisMonth = "312 €",
    ),
    analytics = ProfileAnalytics(
        groupCount = 5,
        totalSpent = "1.284 €",
        spentDelta = "+8% vs 90 días previos",
        isDeltaPositive = true,
        averageTicket = "18,60 €",
        expenseCount = 69,
    ),
    categories = previewCategories(),
    groupComparisons = previewGroupComparisons(),
    account = ProfileAccount(paymentMethod = "Bizum"),
)

private fun previewCategories() = persistentListOf(
    previewCategory("Compra", Res.drawable.bag, DebtshareColors.Accent.mustardDark, "462 €", 36),
    previewCategory("Restaurantes", Res.drawable.cup, DebtshareColors.Accent.plum, "298 €", 23),
    previewCategory("Casa", Res.drawable.house, DebtshareColors.Brand.primary, "214 €", 17),
    previewCategory("Transporte", Res.drawable.car, DebtshareColors.Semantic.success, "142 €", 11),
    previewCategory("Ocio", Res.drawable.star, DebtshareColors.Brand.secondary, "98 €", 8),
    previewCategory("Otros", Res.drawable.dots, DebtshareColors.Neutral.n400, "70 €", 5),
)

private fun previewCategory(
    name: String,
    icon: DrawableResource,
    color: Color,
    amount: String,
    percentage: Int,
) = ProfileCategory(
    name = name,
    icon = icon,
    iconTint = color,
    barColor = color,
    amount = amount,
    percentage = percentage,
    barProgress = percentage / 100f,
)

private fun previewGroupComparisons() = persistentListOf(
    ProfileGroupComparison(
        id = "1",
        name = "Piso Castellana 43",
        icon = Res.drawable.house,
        iconTint = DebtshareColors.Brand.primary,
        barColor = DebtshareColors.Brand.primary,
        memberCount = 4,
        expenseCount = 18,
        totalAmount = "548 €",
        balance = ProfileGroupBalance.Positive("+48,00 €"),
        barProgress = 1f,
    ),
    ProfileGroupComparison(
        id = "2",
        name = "Viaje Roma 2026",
        icon = Res.drawable.star,
        iconTint = DebtshareColors.Accent.mustardDark,
        barColor = DebtshareColors.Accent.mustardDark,
        memberCount = 6,
        expenseCount = 12,
        totalAmount = "384 €",
        balance = ProfileGroupBalance.Negative("−32,00 €"),
        barProgress = 0.7f,
    ),
    ProfileGroupComparison(
        id = "3",
        name = "Cena cumpleaños Pablo",
        icon = Res.drawable.cup,
        iconTint = DebtshareColors.Accent.plum,
        barColor = DebtshareColors.Accent.plum,
        memberCount = 8,
        expenseCount = 3,
        totalAmount = "198 €",
        balance = ProfileGroupBalance.Positive("+24,50 €"),
        barProgress = 0.36f,
    ),
    ProfileGroupComparison(
        id = "4",
        name = "Coworking Julio",
        icon = Res.drawable.house,
        iconTint = DebtshareColors.Semantic.success,
        barColor = DebtshareColors.Semantic.success,
        memberCount = 3,
        expenseCount = 5,
        totalAmount = "108 €",
        balance = ProfileGroupBalance.Settled,
        barProgress = 0.2f,
    ),
    ProfileGroupComparison(
        id = "5",
        name = "Finde Sierra",
        icon = Res.drawable.star,
        iconTint = DebtshareColors.Brand.secondary,
        barColor = DebtshareColors.Brand.secondary,
        memberCount = 5,
        expenseCount = 4,
        totalAmount = "46 €",
        balance = ProfileGroupBalance.Settled,
        barProgress = 0.08f,
    ),
)
