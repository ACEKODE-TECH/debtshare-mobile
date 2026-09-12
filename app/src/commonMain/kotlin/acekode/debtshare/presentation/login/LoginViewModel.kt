package acekode.debtshare.presentation.login

import acekode.debtshare.data.datasource.remote.NetworkException
import acekode.debtshare.domain.usecase.LoginUseCase
import acekode.debtshare.googleAuth.GoogleSignInResult
import acekode.debtshare.presentation.ValidationError
import acekode.debtshare.utils.logDebug
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

private val emailRegex = Regex("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")

@KoinViewModel
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    val uiState: StateFlow<LoginUiState>
        field = MutableStateFlow<LoginUiState>(LoginUiState.Idle)

    val fieldErrors: StateFlow<LoginFieldErrors>
        field = MutableStateFlow(LoginFieldErrors())

    fun onEmailChange() {
        fieldErrors.update { it.copy(emailError = null) }
    }

    fun onPasswordChange() {
        fieldErrors.update { it.copy(passwordError = null) }
    }

    fun onLoginClick(email: String, password: String, rememberMe: Boolean) {
        val emailError = when {
            email.isBlank() -> ValidationError.Required
            !emailRegex.matches(email) -> ValidationError.InvalidFormat
            else -> null
        }
        val passwordError = if (password.isBlank()) ValidationError.Required else null

        if (emailError != null || passwordError != null) {
            fieldErrors.value = LoginFieldErrors(emailError = emailError, passwordError = passwordError)
            return
        }

        viewModelScope.launch {
            uiState.value = LoginUiState.Loading
            loginUseCase(email, password, rememberMe)
                .onSuccess { uiState.value = LoginUiState.Success }
                .onFailure { error ->
                    val loginError = when (error) {
                        is NetworkException.Unauthorized -> LoginError.Unauthorized
                        is NetworkException.Forbidden -> LoginError.Forbidden
                        is NetworkException.Timeout -> LoginError.Timeout
                        is NetworkException.ServiceUnavailable -> LoginError.ServiceUnavailable
                        else -> LoginError.Unknown
                    }
                    uiState.value = LoginUiState.Error(loginError)
                }
        }
    }

    fun onGoogleSignIn(result: GoogleSignInResult) {
        logDebug("LoginViewModel", "Google sign in result: $result")
    }

    fun onForgotPasswordClick() {
        logDebug("LoginViewModel", "Forgot password clicked")
    }
}
