package acekode.debtshare.presentation

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ValidationError {
    data object Required : ValidationError
    data object InvalidFormat : ValidationError
}
