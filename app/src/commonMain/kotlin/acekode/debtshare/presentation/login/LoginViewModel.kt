package acekode.debtshare.presentation.login

import acekode.debtshare.auth.GoogleSignInResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

//    private val _navEvent = MutableSharedFlow<NavEvent>()
//    val navEvent: SharedFlow<NavEvent> = _navEvent.asSharedFlow()

    fun onLoginClick(email: String, password: String, rememberMe: Boolean) {
        // Handle Login Click
    }

    fun onGoogleSignIn(result: GoogleSignInResult) {
        // Handle Google Sign In
    }

    fun onForgotPasswordClick() {
        // Handle Password Click
    }

    fun onCreateAccountClick() {
        // Handle Create Account Click
    }
}
