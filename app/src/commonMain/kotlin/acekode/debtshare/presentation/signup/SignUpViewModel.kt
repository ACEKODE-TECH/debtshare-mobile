package acekode.debtshare.presentation.signup

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
class SignUpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _aliasValidation = MutableStateFlow<AliasValidation>(AliasValidation.Idle)
    val aliasValidation: StateFlow<AliasValidation> = _aliasValidation.asStateFlow()

    fun onAliasChange(alias: String) {
        // Trigger debounced alias availability check
    }

    fun onSignUpClick(alias: String, email: String, password: String, termsAccepted: Boolean) {
        // Handle sign up
    }

    fun onTermsClick() {
        // Open terms
    }

    fun onPrivacyPolicyClick() {
        // Open privacy policy
    }
}
