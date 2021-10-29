package com.test.register.ui

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

class RegisterViewModel(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository
) : BaseViewModel(savedStateHandle) {

    sealed class Event {
        object ClearErrorTextColor : Event()

        object ToRegister : Event()
    }

    private val _eventsChannel = Channel<Event>(capacity = Channel.CONFLATED)
    val eventsFlow = _eventsChannel.receiveAsFlow()

    val emailInputFlow = MutableStateFlow("")
    val nameInputFlow = MutableStateFlow("")
    val lastNameInputFlow = MutableStateFlow("")
    val passwordInputFlow = MutableStateFlow("")
    val confirmPasswordInputFlow = MutableStateFlow("")

    // TODO: 29.10.2021 Extract in separate model (or enum) to reduce local variables
    var wrongEmail = false
    var wrongName = false
    var wrongLastName = false
    var passwordsNotMatch = false
    var wrongPassword = false
    var wrongPasswordConfirm = false

    private var isDataCorrect = true

    val isDataValidFlow = combine(
        emailInputFlow,
        nameInputFlow,
        lastNameInputFlow,
        passwordInputFlow,
        confirmPasswordInputFlow
    ) { email, name, lastName, password, confirmPassword ->
        _eventsChannel.trySend(Event.ClearErrorTextColor)

        isDataCorrect = true
        wrongEmail = false
        wrongName = false
        wrongLastName = false
        passwordsNotMatch = false
        wrongPassword = false
        wrongPasswordConfirm = false

        if (email.length < 6) {
            isDataCorrect = false
            wrongEmail = true
        }
        if (name.length < 4) {
            isDataCorrect = false
            wrongName = true
        }
        if (lastName.length < 4) {
            isDataCorrect = false
            wrongLastName = true
        }
        if (password != confirmPassword) {
            isDataCorrect = false
            passwordsNotMatch = true
        }
        if (password.length < 6) {
            isDataCorrect = false
            wrongPassword = true
        }
        if (confirmPassword.length < 6) {
            isDataCorrect = false
            wrongPasswordConfirm = true
        }

        if ((password.any { it.isDigit() } && password.any { it.isLetter() }).not()) {
            isDataCorrect = false
            wrongPassword = true
        }

        if ((confirmPassword.any { it.isDigit() } && confirmPassword.any { it.isLetter() }).not()) {
            isDataCorrect = false
            wrongPasswordConfirm = true
        }
        isDataCorrect
    }.stateIn(viewModelScope, WhileViewSubscribed, false)

    fun registerUser() {
        launchRequest {
            authRepository.signUp(
                email = emailInputFlow.value,
                name = nameInputFlow.value,
                lastName = lastNameInputFlow.value,
                password = passwordInputFlow.value
            )
            _eventsChannel.trySend(Event.ToRegister)
        }
    }

}