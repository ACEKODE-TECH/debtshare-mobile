package acekode.debtshare.presentation.groupdetail

import acekode.debtshare.presentation.home.groups.MemberBadge
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface GroupDetailUiState {
    data object Loading : GroupDetailUiState
    data class Error(val message: String) : GroupDetailUiState
    data class Content(
        val groupName: String,
        val memberBadges: ImmutableList<MemberBadge>,
        val memberCount: Int,
        val createdDate: String,
        val balance: GroupDetailBalance,
        val debtSummary: String,
        val expenseCount: Int,
        val sections: ImmutableList<ExpenseSection>,
    ) : GroupDetailUiState
}
