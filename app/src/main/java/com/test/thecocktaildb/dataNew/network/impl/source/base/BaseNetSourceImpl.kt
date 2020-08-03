package com.test.thecocktaildb.dataNew.network.impl.source.base

import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.internal.bind.TypeAdapters
import com.test.thecocktaildb.core.common.exception.ApiException
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

// TODO: розібратися в цьому класі. Також зрозуміти, навіщо використовувати дженеріки і
//  передавати ApiService
open class BaseNetSourceImpl<ApiService>(private val apiService: ApiService) {

    /**
     * Performs api request and handles api errors.
     *
     * @param request lambda that invokes desire api request.
     * @return api response model.
     */
    @Throws(ApiException::class, Throwable::class)
    protected open suspend fun <ResponseModel : Any?> performRequest(
        request: suspend ApiService.() -> ResponseModel
    ): ResponseModel {
        return try {
            apiService.request()
        } catch (e: Exception) {
            throw parseApiErrorException(e)
        }
    }

    @Throws(Throwable::class)
    protected open fun parseApiErrorException(throwable: Throwable): ApiException {
        val apiException: ApiException

        if (throwable is HttpException) {

            val url = throwable.response()?.raw()?.request?.url.toString()

            val errorBody = throwable.response()?.errorBody()
                ?: throw ApiException(
                    url,
                    ApiException.UNKNOWN_ERROR,
                    "Error body is null!",
                    throwable.code(),
                    throwable
                )

            val (apiErrorCode, details) = errorBody
                .runCatching {
                    TypeAdapters.JSON_ELEMENT
                        .fromJson(errorBody.string()).asJsonObject
                        .run {
                            val code = when {
                                has("code") -> get("code").asInt
                                else -> ApiException.UNKNOWN_ERROR
                            }
                            val details = when {
                                has("details") -> get("details").asString
                                has("message") -> get("message").asString
                                has("status") -> get("status").asString
                                else -> "Unknown error!"
                            }

                            code to details
                        }
                }
                .getOrElse {
                    if (throwable.code() >= 500) {
                        ApiException.SERVER_ERROR to (throwable.localizedMessage ?: throwable.message
                        ?: it.message.orEmpty())
                    } else if (it is IOException || it is JsonSyntaxException) {
                        it.printStackTrace()
                        ApiException.JSON_PARSE to it.message.orEmpty()
                    } else {
                        ApiException.UNKNOWN_ERROR to it.message.orEmpty()
                    }
                }


            apiException = ApiException(url, apiErrorCode, details, throwable.code())

        } else {
            val apiErrorCode: Int = when (throwable) {
                is JsonSyntaxException,
                is JSONException,
                is JsonParseException -> ApiException.JSON_PARSE
                is ConnectException -> ApiException.SERVER_NOT_RESPONDING
                is SocketTimeoutException -> ApiException.SOCKET_TIMEOUT
                else -> throw throwable
            }

            apiException = ApiException("", apiErrorCode, throwable.message.orEmpty(), -1, throwable)
        }

        return apiException
    }

    private fun setStackTrace(apiException: ApiException) {
        apiException.stackTrace = arrayOf(
            StackTraceElement(
                "server error with code=${apiException.code}, httpCode=${apiException.httpCode}\n",
                apiException.details,
                apiException.method,
                0
            )
        )
    }
}