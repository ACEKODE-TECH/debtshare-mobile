package acekode.debtshare.presentation.home.groups.balances

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface BalancesUiState {

    data object Loading : BalancesUiState

    data class Content(
        val groupName: String,
        val simplified: ImmutableList<BalanceCardUiModel>,
        val detailed: ImmutableList<BalanceCardUiModel>,
        val formattedTotal: String,
        val detailedCount: Int,
    ) : BalancesUiState {

        override fun toString(): String = "Content"
    }
}
