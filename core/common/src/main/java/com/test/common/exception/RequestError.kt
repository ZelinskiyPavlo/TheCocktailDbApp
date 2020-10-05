package com.test.common.exception

sealed class RequestError(
    val method: String,
    val code: Int,
    val details: String,
    val httpCode: Int,
    val cause: Throwable? = null
)

class ApiError(
    method: String, code: Int, details: String, httpCode: Int, cause: Throwable? = null
) : RequestError(method, code, details, httpCode, cause)

class LoginError(
    method: String, code: Int, details: String, httpCode: Int, cause: Throwable? = null
) : RequestError(method, code, details, httpCode, cause)

class RegistrationError(
    method: String, code: Int, details: String, httpCode: Int, cause: Throwable? = null
) : RequestError(method, code, details, httpCode, cause)

class UnAuthorizedAccessError(
    method: String, code: Int, details: String, httpCode: Int, cause: Throwable? = null
) : RequestError(method, code, details, httpCode, cause)

class UploadAvatarError(
    method: String, code: Int, details: String, httpCode: Int, cause: Throwable? = null
) : RequestError(method, code, details, httpCode, cause)

class ServerError(
    method: String, code: Int, details: String, httpCode: Int, cause: Throwable? = null
) : RequestError(method, code, details, httpCode, cause)

class ServerRespondingError(
    method: String, code: Int, details: String, httpCode: Int, cause: Throwable? = null
) : RequestError(method, code, details, httpCode, cause)

class UnknownError(
    method: String, code: Int, details: String, httpCode: Int, cause: Throwable? = null
) : RequestError(method, code, details, httpCode, cause)

class CancellationError(
    method: String, code: Int, details: String, httpCode: Int, cause: Throwable? = null
) : RequestError(method, code, details, httpCode, cause)

class NoInternetConnectionError(
    method: String = "",
    code: Int = ApiException.NO_INTERNET_CONNECTION,
    details: String = "no internet connection",
    httpCode: Int = -1,
    cause: Throwable? = null
) : RequestError(method, code, details, httpCode, cause)