package acekode.debtshare.presentation.signup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

private val emailRegex = Regex("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")

@KoinViewModel
class SignUpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _aliasValidation = MutableStateFlow<AliasValidation>(AliasValidation.Idle)
    val aliasValidation: StateFlow<AliasValidation> = _aliasValidation.asStateFlow()

    private val _fieldErrors = MutableStateFlow(SignUpFieldErrors())
    val fieldErrors: StateFlow<SignUpFieldErrors> = _fieldErrors.asStateFlow()

    fun onAliasChange(alias: String) {
        println(alias)
        _fieldErrors.update { it.copy(aliasError = null) }
    }

    fun onEmailChange() {
        _fieldErrors.update { it.copy(emailError = null) }
    }

    fun onPasswordChange() {
        _fieldErrors.update { it.copy(passwordError = null) }
    }

    fun onSignUpClick(alias: String, email: String, password: String, termsAccepted: Boolean) {
        println(termsAccepted)
        val aliasError = if (alias.isBlank()) "Alias is required" else null
        val emailError = when {
            email.isBlank() -> "Email is required"
            !emailRegex.matches(email) -> "Invalid email format"
            else -> null
        }
        val passwordError = if (password.isBlank()) "Password is required" else null

        if (aliasError != null || emailError != null || passwordError != null) {
            _fieldErrors.value = SignUpFieldErrors(
                aliasError = aliasError,
                emailError = emailError,
                passwordError = passwordError,
            )
            return
        }
    }
}
