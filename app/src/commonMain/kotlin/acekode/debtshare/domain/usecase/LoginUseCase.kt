package acekode.debtshare.domain.usecase

import acekode.debtshare.domain.model.AuthSession
import kotlinx.coroutines.delay
import org.koin.core.annotation.Factory

interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String, rememberMe: Boolean): Result<AuthSession>
}

@Factory
class LoginUseCaseImpl : LoginUseCase {
    override suspend fun invoke(
        email: String,
        password: String,
        rememberMe: Boolean,
    ): Result<AuthSession> {
        delay(1000)
        return Result.success(AuthSession(accessToken = "mock-access-token", refreshToken = "mock-refresh-token"))
    }
}
