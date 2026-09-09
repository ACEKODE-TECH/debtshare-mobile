package acekode.debtshare.presentation

import androidx.compose.runtime.Immutable

@Immutable
sealed interface AliasValidation {
    data object Idle : AliasValidation
    data object Checking : AliasValidation
    data object Available : AliasValidation
    data class Taken(val suggestions: List<String>) : AliasValidation

    val isAvailable: Boolean get() = this is Available
    val isTaken: Boolean get() = this is Taken
    val takenSuggestions: List<String> get() = (this as? Taken)?.suggestions.orEmpty()
}
