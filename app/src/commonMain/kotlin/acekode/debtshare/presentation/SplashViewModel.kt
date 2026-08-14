package acekode.debtshare.presentation

import acekode.debtshare.domain.repository.SessionRepository
import acekode.debtshare.utils.logDebug
import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel

private const val TAG = "SplashViewModel"

@KoinViewModel
class SplashViewModel(private val sessionRepository: SessionRepository) : ViewModel() {

    fun checkSession(): Boolean {
        val session = sessionRepository.getSession()
        logDebug(TAG, "Session found: ${session != null}")
        return session != null
    }
}
