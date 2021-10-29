package com.test.login.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.test.presentation.ui.base.BaseViewModel
import com.test.presentation.util.WhileViewSubscribed
import com.test.repository.source.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class LoginViewModel(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository
) : BaseViewModel(savedStateHandle) {

    sealed class Event {
        object ClearErrorTextColor: Event()

        class ToLogin(val isLoginSuccess: Boolean): Event()
    }

    private val _eventsChannel = Channel<Event>(capacity = Channel.CONFLATED)
    val eventsFlow = _eventsChannel.receiveAsFlow()

    val emailInputFlow = MutableStateFlow("")
    val passwordInputFlow = MutableStateFlow("")

    var wrongEmail = false
    var wrongPassword = false

    private var isDataCorrect = true

    val isDataValidFlow = combine(emailInputFlow, passwordInputFlow) { email, password ->
        _eventsChannel.trySend(Event.ClearErrorTextColor)
        isDataCorrect = true
        wrongEmail = false
        wrongPassword = false

        if (email.length < 6) {
            isDataCorrect = false
            wrongEmail = true
        }
        if (password.length < 6) {
            isDataCorrect = false
            wrongPassword = true
        }

        if ((password.any { it.isDigit() } && password.any { it.isLetter() }).not()) {
            isDataCorrect = false
            wrongPassword = true
        }

        isDataCorrect
    }.stateIn(viewModelScope, WhileViewSubscribed, false)

    init {
        setInitialText()
    }

    private fun setInitialText() {
        emailInputFlow.value = "zelinskiypavlo@gmail.com"
        passwordInputFlow.value = "password1"
    }

    fun loginUser() {
        launchRequest {
            val loginStatus = authRepository.signIn(
                email = emailInputFlow.value,
                password = passwordInputFlow.value
            )
            if (loginStatus) {
                _eventsChannel.trySend(Event.ToLogin(loginStatus))
            }
        }
    }

}