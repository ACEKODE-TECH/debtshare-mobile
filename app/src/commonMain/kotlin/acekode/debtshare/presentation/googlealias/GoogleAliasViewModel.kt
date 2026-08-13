package acekode.debtshare.presentation.googlealias

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class GoogleAliasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<GoogleAliasUiState>(GoogleAliasUiState.Idle)
    val uiState: StateFlow<GoogleAliasUiState> = _uiState.asStateFlow()

    private val _aliasValidation = MutableStateFlow<AliasValidation>(AliasValidation.Idle)
    val aliasValidation: StateFlow<AliasValidation> = _aliasValidation.asStateFlow()

    fun onAliasChange(alias: String) {
        // Trigger debounced alias availability check
    }

    fun onContinueClick(alias: String) {
        // Handle continue
    }
}
