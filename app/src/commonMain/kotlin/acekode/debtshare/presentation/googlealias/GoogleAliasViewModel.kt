package acekode.debtshare.presentation.googlealias

import acekode.debtshare.presentation.AliasValidation
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
        println(alias)
    }

    fun onContinueClick(alias: String) {
        println(alias)
    }
}
