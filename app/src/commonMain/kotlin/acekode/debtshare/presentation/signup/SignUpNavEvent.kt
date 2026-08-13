package acekode.debtshare.presentation.signup

sealed interface SignUpNavEvent {
    data object NavigateBack : SignUpNavEvent
}
