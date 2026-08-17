package acekode.debtshare.domain.repository

import acekode.debtshare.domain.model.AuthSession

interface SessionRepository {
    fun saveSession(session: AuthSession, rememberMe: Boolean)
    fun getSession(): AuthSession?
    fun clearSession()
}
