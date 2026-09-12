package acekode.debtshare.presentation.googlealias

import acekode.debtshare.presentation.AliasValidation
import acekode.debtshare.utils.logDebug
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class GoogleAliasViewModel : ViewModel() {

    val uiState: StateFlow<GoogleAliasUiState>
        field = MutableStateFlow<GoogleAliasUiState>(GoogleAliasUiState.Idle)

    val aliasValidation: StateFlow<AliasValidation>
        field = MutableStateFlow<AliasValidation>(AliasValidation.Idle)

    fun onAliasChange(alias: String) {
        logDebug("GoogleAliasViewModel", "Alias changed: $alias")
    }

    fun onContinueClick(alias: String) {
        logDebug("GoogleAliasViewModel", "Continue clicked with alias: $alias")
    }
}
