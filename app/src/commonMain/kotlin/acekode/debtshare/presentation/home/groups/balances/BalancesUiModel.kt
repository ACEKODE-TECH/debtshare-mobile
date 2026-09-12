package acekode.debtshare.presentation.home.groups.balances

import androidx.compose.runtime.Immutable

enum class BalancesTab { Simplified, Detailed }

@Immutable
sealed interface BalanceCardType {
    data object YouOwe : BalanceCardType
    data object OwedToYou : BalanceCardType
    data object ThirdParty : BalanceCardType
}

@Immutable
data class BalanceCardUiModel(
    val id: String,
    val fromName: String,
    val toName: String,
    val formattedAmount: String,
    val type: BalanceCardType,
)
