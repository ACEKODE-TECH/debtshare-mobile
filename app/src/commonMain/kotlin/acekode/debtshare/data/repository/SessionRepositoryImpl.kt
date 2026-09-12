package acekode.debtshare.data.repository

import acekode.debtshare.data.datasource.local.session.SessionLocalDataSource
import acekode.debtshare.domain.model.AuthSession
import acekode.debtshare.domain.repository.SessionRepository
import org.koin.core.annotation.Single

@Single
class SessionRepositoryImpl(
    private val sessionLocalDataSource: SessionLocalDataSource,
) : SessionRepository {
    private var inMemorySession: AuthSession? = null

    override fun saveSession(session: AuthSession, rememberMe: Boolean) {
        inMemorySession = null
        sessionLocalDataSource.clearSession()
    }

    override fun getSession(): AuthSession? = null

    override fun clearSession() {
        inMemorySession = null
        sessionLocalDataSource.clearSession()
    }
}
