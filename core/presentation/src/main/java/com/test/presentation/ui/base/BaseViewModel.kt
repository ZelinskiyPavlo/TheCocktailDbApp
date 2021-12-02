package com.test.presentation.ui.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.common.constant.BaseUrl
import com.test.common.exception.ApiError
import com.test.common.exception.ApiException
import com.test.common.exception.HttpException
import com.test.common.exception.LoginError
import com.test.common.exception.NoInternetConnectionError
import com.test.common.exception.RegistrationError
import com.test.common.exception.RequestError
import com.test.common.exception.ServerError
import com.test.common.exception.ServerRespondingError
import com.test.common.exception.UnAuthorizedAccessError
import com.test.common.exception.UnknownError
import com.test.common.exception.UploadAvatarError
import com.test.presentation.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _errorFlow = MutableSharedFlow<RequestError>()
    val errorFlow = _errorFlow.asSharedFlow()

    protected fun launchRequest(
        context: CoroutineContext = Dispatchers.IO,
        request: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch {
            try {
                withContext(context) { request() }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(e: Exception) {
        if (BuildConfig.DEBUG) e.printStackTrace()

        if (e is ApiException) {
            // TODO: 10.02.2021 Maybe i need additional class for Errors, to remove manual error check in ViewModel??
            val error = when {
                e.code == HttpException.BAD_REQUEST
                        && e.method == BaseUrl.Auth.plus("login") -> {
                    LoginError(e.method, e.code, e.details, e.httpCode, e.cause)
                }
                e.code == HttpException.BAD_REQUEST
                        && e.method == BaseUrl.Auth.plus("avatar") -> {
                    UploadAvatarError(e.method, e.code, e.details, e.httpCode, e.cause)
                }
                e.code == HttpException.UNAUTHORIZED -> {
                    RegistrationError(e.method, e.code, e.details, e.httpCode, e.cause)
                }
                e.code == HttpException.TOKEN_REQUIRED -> {
                    UnAuthorizedAccessError(e.method, e.code, e.details, e.httpCode, e.cause)
                }
                e.code == ApiException.JSON_PARSE -> {
                    ApiError(e.method, e.code, e.details, e.httpCode, e.cause)
                }
                e.code == ApiException.SERVER_ERROR -> {
                    ServerError(e.method, e.code, e.details, e.httpCode, e.cause)
                }
                e.code == ApiException.SERVER_NOT_RESPONDING
                        || e.code == ApiException.SOCKET_TIMEOUT -> {
                    ServerRespondingError(e.method, e.code, e.details, e.httpCode, e.cause)
                }
                e.code == ApiException.UNKNOWN_ERROR -> {
                    UnknownError(e.method, e.code, e.details, e.httpCode, e.cause)
                }
                e.code == ApiException.NO_INTERNET_CONNECTION -> {
                    NoInternetConnectionError(e.method, e.code, e.details, e.httpCode, e.cause)
                }
                else -> {
                    throw IllegalArgumentException("Unknown error caught")
                }
            }
            _errorFlow.tryEmit(error)
        }
    }
}