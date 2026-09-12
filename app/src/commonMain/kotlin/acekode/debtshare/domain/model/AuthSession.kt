package acekode.debtshare.domain.model

data class AuthSession(
    val accessToken: String,
    val refreshToken: String,
)
