package acekode.debtshare.presentation.home.groups.balances

import acekode.debtshare.ui.items.DebtshareAvatar
import acekode.debtshare.ui.items.DebtshareAvatarSize
import acekode.debtshare.ui.items.DebtshareAvatarState
import acekode.debtshare.ui.items.DebtshareButton
import acekode.debtshare.ui.items.DebtshareButtonSize
import acekode.debtshare.ui.items.DebtshareButtonVariant
import acekode.debtshare.ui.items.DebtshareIcon
import acekode.debtshare.ui.items.DebtshareIconSize
import acekode.debtshare.ui.items.DebtshareTabItem
import acekode.debtshare.ui.items.DebtshareTabs
import acekode.debtshare.ui.items.DebtshareTabsVariant
import acekode.debtshare.ui.theme.DebtshareColors
import acekode.debtshare.ui.theme.DebtshareTheme
import acekode.debtshare.ui.utils.DebtshareScreenPreview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.arrow_left
import debtshare.app.generated.resources.balances_confirm_payment
import debtshare.app.generated.resources.balances_footer_subtitle
import debtshare.app.generated.resources.balances_footer_title
import debtshare.app.generated.resources.balances_remind
import debtshare.app.generated.resources.balances_settle
import debtshare.app.generated.resources.balances_tab_detailed
import debtshare.app.generated.resources.balances_tab_simplified
import debtshare.app.generated.resources.balances_title
import debtshare.app.generated.resources.balances_you_label
import debtshare.app.generated.resources.balances_your_payment
import debtshare.app.generated.resources.bell
import debtshare.app.generated.resources.check
import debtshare.app.generated.resources.check_circle
import debtshare.app.generated.resources.chevron_right
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BalancesScreen(
    uiState: BalancesUiState,
    onBackClick: () -> Unit,
    onConfirmPaymentClick: (BalanceCardUiModel) -> Unit,
    onRemindClick: (BalanceCardUiModel) -> Unit,
    onSettleClick: (BalanceCardUiModel) -> Unit,
) {
    when (uiState) {
        is BalancesUiState.Loading -> {}

        is BalancesUiState.Content -> BalancesContent(
            uiState = uiState,
            onBackClick = onBackClick,
            onConfirmPaymentClick = onConfirmPaymentClick,
            onRemindClick = onRemindClick,
            onSettleClick = onSettleClick,
        )
    }
}

@Composable
private fun BalancesContent(
    uiState: BalancesUiState.Content,
    onBackClick: () -> Unit,
    onConfirmPaymentClick: (BalanceCardUiModel) -> Unit,
    onRemindClick: (BalanceCardUiModel) -> Unit,
    onSettleClick: (BalanceCardUiModel) -> Unit,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val selectedTab = if (selectedTabIndex == 0) BalancesTab.Simplified else BalancesTab.Detailed
    val cards = if (selectedTab == BalancesTab.Simplified) uiState.simplified else uiState.detailed
    val sortedCards = remember(cards) {
        cards.sortedWith(compareBy { it.type !is BalanceCardType.YouOwe })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DebtshareTheme.colors.background),
    ) {
        BalancesTopBar(
            groupName = uiState.groupName,
            onBackClick = onBackClick,
        )

        Spacer(Modifier.height(8.dp))

        DebtshareTabs(
            tabs = persistentListOf(
                DebtshareTabItem(label = stringResource(Res.string.balances_tab_simplified)),
                DebtshareTabItem(label = stringResource(Res.string.balances_tab_detailed)),
            ),
            selectedIndex = selectedTabIndex,
            onSelect = { selectedTabIndex = it },
            variant = DebtshareTabsVariant.Segmented,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(sortedCards, key = { it.id }) { card ->
                BalanceCard(
                    card = card,
                    onConfirmPaymentClick = onConfirmPaymentClick,
                    onRemindClick = onRemindClick,
                    onSettleClick = onSettleClick,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            item { Spacer(Modifier.height(16.dp)) }
        }

        if (selectedTab == BalancesTab.Simplified) {
            BalancesFooter(
                simplifiedCount = uiState.simplified.size,
                detailedCount = uiState.detailedCount,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 24.dp),
            )
        }
    }
}

@Composable
private fun BalancesTopBar(
    groupName: String,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DebtshareIcon(icon = Res.drawable.arrow_left, onClick = onBackClick)

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = groupName,
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareTheme.colors.textTertiary,
            )
            Text(
                text = stringResource(Res.string.balances_title),
                style = DebtshareTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = DebtshareTheme.colors.textPrimary,
            )
        }

        Spacer(Modifier.size(24.dp))
    }
}

@Composable
private fun BalanceCard(
    card: BalanceCardUiModel,
    onConfirmPaymentClick: (BalanceCardUiModel) -> Unit,
    onRemindClick: (BalanceCardUiModel) -> Unit,
    onSettleClick: (BalanceCardUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isYouOwe = card.type is BalanceCardType.YouOwe
    val borderColor = if (isYouOwe) {
        DebtshareColors.Semantic.success.copy(alpha = 0.3f)
    } else {
        DebtshareTheme.colors.border
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .background(DebtshareTheme.colors.card)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isYouOwe) {
            Text(
                text = stringResource(Res.string.balances_your_payment),
                style = DebtshareTheme.typography.labelUppercase,
                color = DebtshareColors.Semantic.success,
                modifier = Modifier.align(Alignment.Start),
            )
            Spacer(Modifier.height(12.dp))
        }

        TransferRow(
            fromName = card.fromName,
            toName = card.toName,
            formattedAmount = card.formattedAmount,
            isFromCurrentUser = isYouOwe,
            isToCurrentUser = card.type is BalanceCardType.OwedToYou,
        )

        Spacer(Modifier.height(12.dp))

        CardActionButton(
            card = card,
            onConfirmPaymentClick = onConfirmPaymentClick,
            onRemindClick = onRemindClick,
            onSettleClick = onSettleClick,
        )
    }
}

@Composable
private fun TransferRow(
    fromName: String,
    toName: String,
    formattedAmount: String,
    isFromCurrentUser: Boolean,
    isToCurrentUser: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        AvatarWithLabel(
            name = fromName,
            isCurrentUser = isFromCurrentUser,
            modifier = Modifier.weight(1f),
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formattedAmount,
                style = DebtshareTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = DebtshareTheme.colors.textPrimary,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(DebtshareColors.Brand.primary, RoundedCornerShape(2.dp)),
                    )
                }
                Icon(
                    painter = painterResource(Res.drawable.chevron_right),
                    contentDescription = null,
                    tint = DebtshareColors.Brand.primary,
                    modifier = Modifier.size(12.dp),
                )
            }
        }

        AvatarWithLabel(
            name = toName,
            isCurrentUser = isToCurrentUser,
            modifier = Modifier.weight(1f),
            alignEnd = true,
        )
    }
}

@Composable
private fun AvatarWithLabel(
    name: String,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier,
    alignEnd: Boolean = false,
) {
    val alignment = if (alignEnd) Alignment.End else Alignment.Start
    Column(
        modifier = modifier,
        horizontalAlignment = alignment,
    ) {
        DebtshareAvatar(
            name = name,
            size = DebtshareAvatarSize.Large,
            state = if (isCurrentUser) DebtshareAvatarState.CurrentUser else DebtshareAvatarState.Default,
        )
        Spacer(Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = name.substringBefore(" "),
                style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = DebtshareTheme.colors.textPrimary,
            )
            if (isCurrentUser) {
                Text(
                    text = stringResource(Res.string.balances_you_label),
                    style = DebtshareTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                    ),
                    color = DebtshareColors.Brand.primary,
                    modifier = Modifier
                        .background(
                            DebtshareColors.Brand.primaryTint,
                            RoundedCornerShape(4.dp),
                        )
                        .padding(horizontal = 4.dp, vertical = 1.dp),
                )
            }
        }
    }
}

@Composable
private fun CardActionButton(
    card: BalanceCardUiModel,
    onConfirmPaymentClick: (BalanceCardUiModel) -> Unit,
    onRemindClick: (BalanceCardUiModel) -> Unit,
    onSettleClick: (BalanceCardUiModel) -> Unit,
) {
    when (card.type) {
        is BalanceCardType.OwedToYou -> DebtshareButton(
            text = stringResource(Res.string.balances_confirm_payment),
            onClick = { onConfirmPaymentClick(card) },
            variant = DebtshareButtonVariant.Secondary,
            icon = Res.drawable.check,
            size = DebtshareButtonSize.Medium,
            modifier = Modifier.fillMaxWidth(),
        )

        is BalanceCardType.ThirdParty -> DebtshareButton(
            text = stringResource(Res.string.balances_remind, card.fromName.substringBefore(" ")),
            onClick = { onRemindClick(card) },
            variant = DebtshareButtonVariant.Secondary,
            icon = Res.drawable.bell,
            size = DebtshareButtonSize.Medium,
            modifier = Modifier.fillMaxWidth(),
        )

        is BalanceCardType.YouOwe -> DebtshareButton(
            text = stringResource(Res.string.balances_settle, card.formattedAmount),
            onClick = { onSettleClick(card) },
            variant = DebtshareButtonVariant.Primary,
            icon = Res.drawable.chevron_right,
            size = DebtshareButtonSize.Large,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun BalancesFooter(
    simplifiedCount: Int,
    detailedCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DebtshareColors.Semantic.successTintSoft)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DebtshareIcon(
            icon = Res.drawable.check_circle,
            tint = DebtshareColors.Semantic.success,
            size = DebtshareIconSize.Medium,
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = stringResource(Res.string.balances_footer_title, simplifiedCount),
                style = DebtshareTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = DebtshareColors.Semantic.success,
            )
            Text(
                text = stringResource(Res.string.balances_footer_subtitle, detailedCount),
                style = DebtshareTheme.typography.bodySmall,
                color = DebtshareColors.Semantic.success,
            )
        }
    }
}

private fun previewContent() = BalancesUiState.Content(
    groupName = "Piso Castellana 43",
    simplified = persistentListOf(
        BalanceCardUiModel(
            id = "1",
            fromName = "Carlos Ruiz",
            toName = "Ana García",
            formattedAmount = "48,00 €",
            type = BalanceCardType.OwedToYou,
        ),
        BalanceCardUiModel(
            id = "2",
            fromName = "Marta López",
            toName = "Luis Prat",
            formattedAmount = "24,50 €",
            type = BalanceCardType.ThirdParty,
        ),
        BalanceCardUiModel(
            id = "3",
            fromName = "Ana García",
            toName = "Luis Prat",
            formattedAmount = "12,00 €",
            type = BalanceCardType.YouOwe,
        ),
    ),
    detailed = persistentListOf(
        BalanceCardUiModel(
            id = "d1",
            fromName = "Carlos Ruiz",
            toName = "Ana García",
            formattedAmount = "30,00 €",
            type = BalanceCardType.OwedToYou,
        ),
        BalanceCardUiModel(
            id = "d2",
            fromName = "Carlos Ruiz",
            toName = "Luis Prat",
            formattedAmount = "18,00 €",
            type = BalanceCardType.ThirdParty,
        ),
        BalanceCardUiModel(
            id = "d3",
            fromName = "Marta López",
            toName = "Ana García",
            formattedAmount = "15,00 €",
            type = BalanceCardType.OwedToYou,
        ),
        BalanceCardUiModel(
            id = "d4",
            fromName = "Marta López",
            toName = "Luis Prat",
            formattedAmount = "9,50 €",
            type = BalanceCardType.ThirdParty,
        ),
        BalanceCardUiModel(
            id = "d5",
            fromName = "Ana García",
            toName = "Luis Prat",
            formattedAmount = "12,00 €",
            type = BalanceCardType.YouOwe,
        ),
    ),
    formattedTotal = "84,50 €",
    detailedCount = 8,
)

@DebtshareScreenPreview
@Composable
private fun BalancesScreenPreview() {
    DebtshareTheme(darkTheme = false) {
        BalancesScreen(
            uiState = previewContent(),
            onBackClick = {},
            onConfirmPaymentClick = {},
            onRemindClick = {},
            onSettleClick = {},
        )
    }
}

@DebtshareScreenPreview
@Composable
private fun BalancesScreenDarkPreview() {
    DebtshareTheme(darkTheme = true) {
        BalancesScreen(
            uiState = previewContent(),
            onBackClick = {},
            onConfirmPaymentClick = {},
            onRemindClick = {},
            onSettleClick = {},
        )
    }
}
