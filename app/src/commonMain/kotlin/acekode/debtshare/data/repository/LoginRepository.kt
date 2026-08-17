package acekode.debtshare.data.repository

import acekode.debtshare.data.datasource.remote.login.LoginRemoteDataSource
import acekode.debtshare.domain.model.AuthSession
import org.koin.core.annotation.Single

interface LoginRepository {
    suspend fun login(email: String, password: String, rememberMe: Boolean): Result<AuthSession>
}

@Single
class LoginRepositoryImpl(
    private val loginRemoteDataSource: LoginRemoteDataSource,
) : LoginRepository {
    override suspend fun login(
        email: String,
        password: String,
        rememberMe: Boolean,
    ): Result<AuthSession> = loginRemoteDataSource.login(email, password, rememberMe)
}
