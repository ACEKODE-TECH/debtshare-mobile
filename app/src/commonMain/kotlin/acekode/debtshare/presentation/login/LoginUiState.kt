package acekode.debtshare.presentation.login

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Error(val message: String) : LoginUiState
}

data class LoginFieldErrors(
    val emailError: String? = null,
    val passwordError: String? = null,
)
