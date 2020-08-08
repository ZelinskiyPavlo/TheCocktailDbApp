package com.test.thecocktaildb.ui.base

import androidx.lifecycle.*
import com.test.thecocktaildb.BuildConfig
import com.test.thecocktaildb.core.common.exception.*
import com.test.thecocktaildb.dataNew.network.NetConstant
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(val savedStateHandle: SavedStateHandle) : ViewModel() {

    val errorLiveData: LiveData<RequestError?> = MutableLiveData()

    protected fun <T> launchRequest(
        liveData: LiveData<T>? = null,
        context: CoroutineContext = Dispatchers.IO,
        request: suspend CoroutineScope.() -> T
    ): Job {
        return viewModelScope.launch {
            try {
                val result: T = withContext(context) { request() }
                liveData?.setValue(result)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private suspend fun handleError(e: Exception) {
        if (BuildConfig.DEBUG) e.printStackTrace()

        if (e is ApiException) {
            val error = when {
                e.code == HttpException.BAD_REQUEST
                        && e.method == NetConstant.Base_Url.Auth.plus("login") -> {
                    LoginError(e.method, e.code, e.details, e.httpCode, e.cause)
                }
                e.code == HttpException.BAD_REQUEST
                        && e.method == NetConstant.Base_Url.Auth.plus("avatar") -> {
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
            withContext(Dispatchers.Main) {
                errorLiveData.setValue(error)
                errorLiveData.setValue(null)
            }
        }
    }

    protected fun <T> LiveData<T>.setValue(value: T) {
        (this as? MutableLiveData)?.value = value
    }

    protected fun <T> LiveData<T>.postValue(value: T) {
        (this as? MutableLiveData)?.postValue(value)
    }

}