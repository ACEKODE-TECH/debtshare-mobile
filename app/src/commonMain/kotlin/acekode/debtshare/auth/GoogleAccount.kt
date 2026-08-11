package acekode.debtshare.auth

data class GoogleAccount(
    val id: String,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
)
