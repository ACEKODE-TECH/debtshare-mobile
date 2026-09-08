package acekode.debtshare.presentation.login

import acekode.debtshare.presentation.ValidationError
import androidx.compose.runtime.Immutable

@Immutable
sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Error(val message: String) : LoginUiState

    val isLoading: Boolean get() = this is Loading
    val errorMessage: String? get() = (this as? Error)?.message
}

@Immutable
data class LoginFieldErrors(
    val emailError: ValidationError? = null,
    val passwordError: ValidationError? = null,
)
