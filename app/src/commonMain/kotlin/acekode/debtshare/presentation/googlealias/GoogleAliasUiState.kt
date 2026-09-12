package acekode.debtshare.presentation.googlealias

import androidx.compose.runtime.Immutable

@Immutable
sealed interface GoogleAliasUiState {
    data object Idle : GoogleAliasUiState
    data object Loading : GoogleAliasUiState
    data class Error(val message: String) : GoogleAliasUiState

    val isLoading: Boolean get() = this is Loading
    val errorMessage: String? get() = (this as? Error)?.message
}
