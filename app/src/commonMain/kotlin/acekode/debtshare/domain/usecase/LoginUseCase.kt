package acekode.debtshare.domain.usecase

import acekode.debtshare.data.repository.LoginRepository
import acekode.debtshare.domain.model.AuthSession
import acekode.debtshare.domain.repository.SessionRepository
import org.koin.core.annotation.Factory

interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String, rememberMe: Boolean): Result<AuthSession>
}

@Factory
class LoginUseCaseImpl(
    private val loginRepository: LoginRepository,
    private val sessionRepository: SessionRepository,
) : LoginUseCase {
    override suspend fun invoke(
        email: String,
        password: String,
        rememberMe: Boolean,
    ): Result<AuthSession> = loginRepository.login(email, password, rememberMe).also { result ->
        result.onSuccess { token ->
            sessionRepository.saveSession(
                AuthSession(token.accessToken, token.refreshToken),
                rememberMe,
            )
        }
    }
}
