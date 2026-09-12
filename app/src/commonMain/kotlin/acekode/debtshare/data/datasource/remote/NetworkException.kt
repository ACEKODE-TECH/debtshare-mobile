package acekode.debtshare.data.datasource.remote

sealed class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class Timeout(cause: Throwable? = null) : NetworkException("Request timed out", cause)
    class Serialization(message: String, cause: Throwable? = null) : NetworkException(message, cause)
    class Unknown(message: String, cause: Throwable? = null) : NetworkException(message, cause)

    // 4xx
    class BadRequest(cause: Throwable? = null) : NetworkException("Bad request", cause)
    class Unauthorized(cause: Throwable? = null) : NetworkException("Unauthorized", cause)
    class Forbidden(cause: Throwable? = null) : NetworkException("Forbidden", cause)
    class NotFound(cause: Throwable? = null) : NetworkException("Not found", cause)
    class Conflict(cause: Throwable? = null) : NetworkException("Conflict", cause)
    class UnprocessableEntity(cause: Throwable? = null) : NetworkException("Unprocessable entity", cause)
    class TooManyRequests(cause: Throwable? = null) : NetworkException("Too many requests", cause)

    // 5xx
    class InternalServerError(cause: Throwable? = null) : NetworkException("Internal server error", cause)
    class ServiceUnavailable(cause: Throwable? = null) : NetworkException("Service unavailable", cause)
    class GatewayTimeout(cause: Throwable? = null) : NetworkException("Gateway timeout", cause)

    class HttpError(val code: Int, message: String, cause: Throwable? = null) : NetworkException(message, cause)
}
