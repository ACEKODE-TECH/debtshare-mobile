package acekode.debtshare.presentation.groupdetail

import acekode.debtshare.presentation.home.groups.MemberBadge
import acekode.debtshare.ui.theme.DebtshareColors
import androidx.lifecycle.ViewModel
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.bag
import debtshare.app.generated.resources.car
import debtshare.app.generated.resources.cup
import debtshare.app.generated.resources.house
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class GroupDetailViewModel : ViewModel() {

    val uiState: StateFlow<GroupDetailUiState>
        field = MutableStateFlow<GroupDetailUiState>(mockContent())

    val selectedTab: StateFlow<GroupDetailTab>
        field = MutableStateFlow(GroupDetailTab.Expenses)

    fun onTabSelected(tab: GroupDetailTab) {
        selectedTab.value = tab
    }
}

private fun mockContent(): GroupDetailUiState.Content = GroupDetailUiState.Content(
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
                ExpenseItemUiModel.Expense(
                    id = "e2",
                    icon = Res.drawable.car,
                    iconTint = DebtshareColors.Brand.primary,
                    title = "Uber al aeropuerto",
                    subtitle = "Pagó Carlos · reparto entre 2",
                    totalAmount = "24,50 €",
                    balanceImpact = BalanceImpact.Negative("-12,25 €"),
                ),
            ),
        ),
        ExpenseSection(
            dateLabel = "AYER",
            items = persistentListOf(
                ExpenseItemUiModel.Expense(
                    id = "e3",
                    icon = Res.drawable.bag,
                    iconTint = DebtshareColors.Semantic.success,
                    title = "Mercadona semanal",
                    subtitle = "Pagó Marta · reparto entre 4",
                    totalAmount = "84,20 €",
                    balanceImpact = BalanceImpact.Negative("-21,05 €"),
                ),
                ExpenseItemUiModel.Settlement(
                    id = "s1",
                    title = "Luis pagó a Ana",
                    subtitle = "Liquidación · Bizum",
                    amount = "30,00 €",
                ),
            ),
        ),
        ExpenseSection(
            dateLabel = "LUN, 18 AGO",
            items = persistentListOf(
                ExpenseItemUiModel.Expense(
                    id = "e4",
                    icon = Res.drawable.house,
                    iconTint = DebtshareColors.Accent.mustardDark,
                    title = "Airbnb Granada",
                    subtitle = "Pagó Ana · reparto entre 4",
                    totalAmount = "320,00 €",
                    balanceImpact = BalanceImpact.Positive("+240,00 €"),
                ),
            ),
        ),
    ),
)
