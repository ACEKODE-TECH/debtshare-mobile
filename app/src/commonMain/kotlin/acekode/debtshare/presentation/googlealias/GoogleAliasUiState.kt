package acekode.debtshare.presentation.googlealias

sealed interface GoogleAliasUiState {
    data object Idle : GoogleAliasUiState
    data object Loading : GoogleAliasUiState
    data class Error(val message: String) : GoogleAliasUiState
}

sealed interface AliasValidation {
    data object Idle : AliasValidation
    data object Checking : AliasValidation
    data object Available : AliasValidation
    data class Taken(val suggestions: List<String>) : AliasValidation
}
