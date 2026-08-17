package acekode.debtshare.presentation.login

import acekode.debtshare.data.datasource.remote.NetworkException
import acekode.debtshare.domain.usecase.LoginUseCase
import acekode.debtshare.googleAuth.GoogleSignInResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

private val emailRegex = Regex("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")

@KoinViewModel
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _fieldErrors = MutableStateFlow(LoginFieldErrors())
    val fieldErrors: StateFlow<LoginFieldErrors> = _fieldErrors.asStateFlow()

    fun onEmailChange() {
        _fieldErrors.update { it.copy(emailError = null) }
    }

    fun onPasswordChange() {
        _fieldErrors.update { it.copy(passwordError = null) }
    }

    fun onLoginClick(email: String, password: String, rememberMe: Boolean) {
        val emailError = when {
            email.isBlank() -> "Email is required"
            !emailRegex.matches(email) -> "Invalid email format"
            else -> null
        }
        val passwordError = if (password.isBlank()) "Password is required" else null

        if (emailError != null || passwordError != null) {
            _fieldErrors.value = LoginFieldErrors(emailError = emailError, passwordError = passwordError)
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            loginUseCase(email, password, rememberMe)
                .onSuccess { _uiState.value = LoginUiState.Idle }
                .onFailure { error ->
                    val message = when (error) {
                        is NetworkException.Unauthorized -> "Incorrect email or password"
                        is NetworkException.Forbidden -> "You don't have permission to access"
                        is NetworkException.Timeout -> "No connection, please try again"
                        is NetworkException.ServiceUnavailable -> "Service unavailable, please try later"
                        else -> "An unexpected error occurred"
                    }
                    _uiState.value = LoginUiState.Error(message)
                }
        }
    }

    fun onGoogleSignIn(result: GoogleSignInResult) {
        println(result)
    }

    fun onForgotPasswordClick() {
        // Handle Password Click
    }
}
