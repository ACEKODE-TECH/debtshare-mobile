package acekode.debtshare.data.datasource.remote.login

import acekode.debtshare.KtorConfigPlugin
import acekode.debtshare.data.datasource.remote.executeApiCall
import acekode.debtshare.data.datasource.remote.login.dto.LoginRequest
import acekode.debtshare.data.datasource.remote.login.dto.LoginResponse
import acekode.debtshare.domain.model.AuthSession
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Single

interface LoginRemoteDataSource {
    suspend fun login(email: String, password: String, rememberMe: Boolean): Result<AuthSession>
}

@Single
class LoginRemoteDataSourceImpl(
    private val httpClient: HttpClient,
) : LoginRemoteDataSource {
    override suspend fun login(
        email: String,
        password: String,
        rememberMe: Boolean,
    ): Result<AuthSession> = executeApiCall {
        val response = httpClient.post("${KtorConfigPlugin.BASE_URL}/api/v1/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password, rememberMe))
        }.body<LoginResponse>()
        AuthSession(accessToken = response.accessToken, refreshToken = response.refreshToken)
    }
}
