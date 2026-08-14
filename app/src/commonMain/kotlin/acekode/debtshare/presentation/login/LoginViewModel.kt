package acekode.debtshare.presentation.login

import acekode.debtshare.auth.GoogleSignInResult
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onLoginClick(email: String, password: String, rememberMe: Boolean) {
        println("$email $password $rememberMe")
    }

    fun onGoogleSignIn(result: GoogleSignInResult) {
        println(result)
    }

    fun onForgotPasswordClick() {
        // Handle Password Click
    }
}
