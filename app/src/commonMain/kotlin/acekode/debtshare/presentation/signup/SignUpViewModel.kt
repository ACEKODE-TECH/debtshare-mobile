package acekode.debtshare.presentation.signup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SignUpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _aliasValidation = MutableStateFlow<AliasValidation>(AliasValidation.Idle)
    val aliasValidation: StateFlow<AliasValidation> = _aliasValidation.asStateFlow()

    fun onAliasChange(alias: String) {
        println(alias)
    }

    fun onSignUpClick(alias: String, email: String, password: String, termsAccepted: Boolean) {
        println("$alias $email $password $termsAccepted")
    }
}
