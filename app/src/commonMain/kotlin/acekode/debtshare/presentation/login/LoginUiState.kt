package acekode.debtshare.presentation.login

import acekode.debtshare.presentation.ValidationError
import androidx.compose.runtime.Immutable
import debtshare.app.generated.resources.Res
import debtshare.app.generated.resources.error_generic
import debtshare.app.generated.resources.error_network_forbidden
import debtshare.app.generated.resources.error_network_timeout
import debtshare.app.generated.resources.error_network_unauthorized
import debtshare.app.generated.resources.error_network_unavailable
import org.jetbrains.compose.resources.StringResource

@Immutable
sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data object Success : LoginUiState
    data class Error(val error: LoginError) : LoginUiState

    val isLoading: Boolean get() = this is Loading
}

enum class LoginError {
    Unauthorized,
    Forbidden,
    Timeout,
    ServiceUnavailable,
    Unknown,
}

fun LoginError.toStringResource(): StringResource = when (this) {
    LoginError.Unauthorized -> Res.string.error_network_unauthorized
    LoginError.Forbidden -> Res.string.error_network_forbidden
    LoginError.Timeout -> Res.string.error_network_timeout
    LoginError.ServiceUnavailable -> Res.string.error_network_unavailable
    LoginError.Unknown -> Res.string.error_generic
}

@Immutable
data class LoginFieldErrors(
    val emailError: ValidationError? = null,
    val passwordError: ValidationError? = null,
)
