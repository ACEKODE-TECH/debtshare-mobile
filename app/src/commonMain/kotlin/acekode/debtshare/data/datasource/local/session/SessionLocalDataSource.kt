package acekode.debtshare.data.datasource.local.session

import acekode.debtshare.domain.model.AuthSession
import com.russhwolf.settings.Settings
import org.koin.core.annotation.Single

interface SessionLocalDataSource {
    fun saveSession(session: AuthSession)
    fun getSession(): AuthSession?
    fun clearSession()
}

@Single
class SessionLocalDataSourceImpl(private val settings: Settings) : SessionLocalDataSource {

    override fun saveSession(session: AuthSession) {
        settings.putString(KEY_ACCESS_TOKEN, session.accessToken)
        settings.putString(KEY_REFRESH_TOKEN, session.refreshToken)
    }

    override fun getSession(): AuthSession? {
        val accessToken = settings.getStringOrNull(KEY_ACCESS_TOKEN) ?: return null
        val refreshToken = settings.getStringOrNull(KEY_REFRESH_TOKEN) ?: return null
        return AuthSession(accessToken, refreshToken)
    }

    override fun clearSession() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
