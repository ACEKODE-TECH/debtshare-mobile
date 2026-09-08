package acekode.debtshare.presentation.home.groups

import androidx.compose.runtime.Immutable

@Immutable
sealed interface GroupsUiState {
    data object Empty : GroupsUiState
    data object Loading : GroupsUiState
    data class Error(val message: String) : GroupsUiState

    val isLoading: Boolean get() = this is Loading
    val errorMessage: String? get() = (this as? Error)?.message
}
