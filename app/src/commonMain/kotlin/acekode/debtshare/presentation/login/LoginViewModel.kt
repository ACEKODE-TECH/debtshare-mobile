package acekode.debtshare.presentation.login

import acekode.debtshare.domain.Greeting
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState(""))
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { state ->
            state.copy(platform = Greeting().greet())
        }
    }
}
