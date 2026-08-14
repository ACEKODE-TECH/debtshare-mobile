package acekode.debtshare.data.datasource.remote.login.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String, val rememberMe: Boolean)
