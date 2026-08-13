package acekode.debtshare.presentation.signup

sealed interface SignUpUiState {
    data object Idle : SignUpUiState
    data object Loading : SignUpUiState
    data class Error(val message: String) : SignUpUiState
}

sealed interface AliasValidation {
    data object Idle : AliasValidation
    data object Checking : AliasValidation
    data object Available : AliasValidation
    data class Taken(val suggestions: List<String>) : AliasValidation
}
