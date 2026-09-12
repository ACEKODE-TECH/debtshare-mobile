package acekode.debtshare.data.datasource.remote

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

@Suppress("TooGenericExceptionCaught")
suspend fun <T> executeApiCall(call: suspend () -> T): Result<T> = try {
    Result.success(call())
} catch (e: ClientRequestException) {
    Result.failure(mapHttpException(e.response.status.value, e))
} catch (e: ServerResponseException) {
    Result.failure(mapHttpException(e.response.status.value, e))
} catch (e: HttpRequestTimeoutException) {
    Result.failure(NetworkException.Timeout(e))
} catch (e: SerializationException) {
    Result.failure(NetworkException.Serialization(e.message.orEmpty(), e))
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    Result.failure(NetworkException.Unknown(e.message.orEmpty(), e))
}

private fun mapHttpException(code: Int, cause: Throwable): NetworkException = when (code) {
    400 -> NetworkException.BadRequest(cause)
    401 -> NetworkException.Unauthorized(cause)
    403 -> NetworkException.Forbidden(cause)
    404 -> NetworkException.NotFound(cause)
    409 -> NetworkException.Conflict(cause)
    422 -> NetworkException.UnprocessableEntity(cause)
    429 -> NetworkException.TooManyRequests(cause)
    500 -> NetworkException.InternalServerError(cause)
    503 -> NetworkException.ServiceUnavailable(cause)
    504 -> NetworkException.GatewayTimeout(cause)
    else -> NetworkException.HttpError(code, "HTTP error $code", cause)
}
