package acekode.debtshare.googleAuth

sealed interface GoogleSignInResult {
    data class Success(val account: GoogleAccount) : GoogleSignInResult

    data class Error(val message: String) : GoogleSignInResult
}
