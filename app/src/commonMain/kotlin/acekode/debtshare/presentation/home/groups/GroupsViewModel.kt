package acekode.debtshare.presentation.home.groups

import acekode.debtshare.ui.theme.DebtshareColors
import androidx.lifecycle.ViewModel
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.car
import debtshare.app.generated.resources.cup
import debtshare.app.generated.resources.house
import debtshare.app.generated.resources.star
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class GroupsViewModel : ViewModel() {

    val uiState: StateFlow<GroupsUiState>
        field = MutableStateFlow<GroupsUiState>(mockContent())

    val selectedFilter: StateFlow<GroupFilter>
        field = MutableStateFlow(GroupFilter.All)

    fun onFilterSelected(filter: GroupFilter) {
        selectedFilter.value = filter
    }
}

private fun mockContent(): GroupsUiState.Content = GroupsUiState.Content(
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
                MemberBadge("JS", DebtshareColors.Brand.primary),
                MemberBadge("PG", DebtshareColors.Accent.mustard),
            ),
        ),
        GroupSummaryUiModel(
            id = "4",
            name = "Escapada Sierra",
            icon = Res.drawable.car,
            iconTint = DebtshareColors.Semantic.success,
            memberCount = 3,
            expenseCount = 6,
            balance = GroupBalance.Positive("+12,00 €"),
            hasActivity = false,
            members = persistentListOf(
                MemberBadge("JS", DebtshareColors.Brand.primary),
                MemberBadge("AM", DebtshareColors.Accent.violet),
                MemberBadge("CR", DebtshareColors.Semantic.success),
            ),
        ),
    ),
)
