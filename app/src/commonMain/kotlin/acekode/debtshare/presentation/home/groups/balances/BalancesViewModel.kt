package acekode.debtshare.presentation.home.groups.balances

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BalancesViewModel : ViewModel() {

    val uiState: StateFlow<BalancesUiState>
        field = MutableStateFlow<BalancesUiState>(mockContent())
}

private fun mockContent(): BalancesUiState.Content = BalancesUiState.Content(
    groupName = "Piso Castellana 43",
    simplified = persistentListOf(
        BalanceCardUiModel(
            id = "s1",
            fromName = "Carlos Ruiz",
            toName = "Ana García",
            formattedAmount = "48,00 €",
            type = BalanceCardType.OwedToYou,
        ),
        BalanceCardUiModel(
            id = "s2",
            fromName = "Marta López",
            toName = "Luis Prat",
            formattedAmount = "24,50 €",
            type = BalanceCardType.ThirdParty,
        ),
        BalanceCardUiModel(
            id = "s3",
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
