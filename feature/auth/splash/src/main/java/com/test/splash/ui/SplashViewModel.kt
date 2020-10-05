package com.test.splash.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.test.presentation.ui.base.BaseViewModel
import com.test.presentation.util.Event
import com.test.repository.source.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

class SplashViewModel(
    savedStateHandle: SavedStateHandle,
    private val userRepo: UserRepository
) : BaseViewModel(savedStateHandle) {

    private val _loginStatusEventLiveData = MutableLiveData<Event<Boolean>>()
    val loginStatusEventLiveData: LiveData<Event<Boolean>> = _loginStatusEventLiveData

    private val _internetStatusEventLiveData = MutableLiveData<Event<Boolean>>()
    val internetStatusEventLiveData: LiveData<Event<Boolean>> = _internetStatusEventLiveData

    fun checkLoginStatus() {
        launchRequest {
            if (userRepo.hasUser()) {
                userRepo.refreshUser()
                _loginStatusEventLiveData.postValue(Event(true))
            } else {
                _loginStatusEventLiveData.postValue(Event(false))
            }
        }
    }

    fun checkInternetConnection() {
        launchRequest {
            runCatching {
                val timeoutMs = 1500
                val sock = Socket()
                val sockAddress: SocketAddress = InetSocketAddress("8.8.8.8", 53)
                sock.connect(sockAddress, timeoutMs)
                sock.close()

                withContext(Dispatchers.Main) {
                    _internetStatusEventLiveData.value = Event(true)
                }
            }.onFailure { exception ->
                when (exception) {
                    is ConnectException ->
                        withContext(Dispatchers.Main) {
                            _internetStatusEventLiveData.value = Event(false)
                        }
                    else -> throw exception
                }
            }
        }
    }
}