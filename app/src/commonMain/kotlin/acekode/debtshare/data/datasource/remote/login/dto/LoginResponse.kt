package acekode.debtshare.data.datasource.remote.login.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val accessToken: String, val refreshToken: String)
