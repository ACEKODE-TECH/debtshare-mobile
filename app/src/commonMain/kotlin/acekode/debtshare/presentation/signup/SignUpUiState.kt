package acekode.debtshare.presentation.signup

import acekode.debtshare.presentation.ValidationError
import androidx.compose.runtime.Immutable

@Immutable
sealed interface SignUpUiState {
    data object Idle : SignUpUiState
    data object Loading : SignUpUiState
    data class Error(val message: String) : SignUpUiState

    val isLoading: Boolean get() = this is Loading
    val errorMessage: String? get() = (this as? Error)?.message
}

@Immutable
data class SignUpFieldErrors(
    val aliasError: ValidationError? = null,
    val emailError: ValidationError? = null,
    val passwordError: ValidationError? = null,
)
