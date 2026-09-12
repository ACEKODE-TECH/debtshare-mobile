package acekode.debtshare.presentation.signup

import acekode.debtshare.presentation.AliasValidation
import acekode.debtshare.presentation.ValidationError
import acekode.debtshare.utils.logDebug
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

private val emailRegex = Regex("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")

@KoinViewModel
class SignUpViewModel : ViewModel() {

    val uiState: StateFlow<SignUpUiState>
        field = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)

    val aliasValidation: StateFlow<AliasValidation>
        field = MutableStateFlow<AliasValidation>(AliasValidation.Idle)

    val fieldErrors: StateFlow<SignUpFieldErrors>
        field = MutableStateFlow(SignUpFieldErrors())

    fun onAliasChange(alias: String) {
        logDebug("SignUpViewModel", "Alias changed: $alias")
        fieldErrors.update { it.copy(aliasError = null) }
    }

    fun onEmailChange() {
        fieldErrors.update { it.copy(emailError = null) }
    }

    fun onPasswordChange() {
        fieldErrors.update { it.copy(passwordError = null) }
    }

    fun onSignUpClick(alias: String, email: String, password: String, termsAccepted: Boolean) {
        val aliasError = if (alias.isBlank()) ValidationError.Required else null
        val emailError = when {
            email.isBlank() -> ValidationError.Required
            !emailRegex.matches(email) -> ValidationError.InvalidFormat
            else -> null
        }
        val passwordError = if (password.isBlank()) ValidationError.Required else null

        if (aliasError != null || emailError != null || passwordError != null) {
            fieldErrors.value = SignUpFieldErrors(
                aliasError = aliasError,
                emailError = emailError,
                passwordError = passwordError,
            )
            return
        }

        logDebug("SignUpViewModel", "Sign up requested for alias=$alias, email=$email, termsAccepted=$termsAccepted")
        uiState.value = SignUpUiState.Loading
    }
}
