package com.test.thecocktaildb.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.test.thecocktaildb.dataNew.repository.source.AuthRepository
import com.test.thecocktaildb.ui.base.BaseViewModel
import com.test.thecocktaildb.util.Event

class LoginViewModel(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository
) : BaseViewModel(savedStateHandle) {

    val emailInputLiveData: MutableLiveData<String?> = MutableLiveData()
    val passwordInputLiveData: MutableLiveData<String?> = MutableLiveData()

    var wrongEmail = false
    var wrongPassword = false

    private val _clearErrorTextColorEventLiveData: MutableLiveData<Event<Unit>> = MutableLiveData()
    val clearErrorTextColorEventLiveData: LiveData<Event<Unit>> = _clearErrorTextColorEventLiveData

    private var isDataCorrect = true

    val isDataValidLiveData: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        fun invalidateTypedData() {
            val typedEmail = emailInputLiveData.value ?: ""
            val typedPassword = passwordInputLiveData.value ?: ""

            _clearErrorTextColorEventLiveData.value = Event(Unit)
            isDataCorrect = true
            wrongEmail = false
            wrongPassword = false

            if (typedEmail.length < 6) {
                isDataCorrect = false
                wrongEmail = true
            }
            if (typedPassword.length < 6) {
                isDataCorrect = false
                wrongPassword = true
            }

            if ((typedPassword.any { it.isDigit() }
                        && typedPassword.any { it.isLetter() }).not()) {
                isDataCorrect = false
                wrongPassword = true
            }

            value = isDataCorrect
        }

        addSource(emailInputLiveData) { invalidateTypedData() }
        addSource(passwordInputLiveData) { invalidateTypedData() }
    }

    private val _loginEventLiveData: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val loginEventLiveData: LiveData<Event<Boolean>> = _loginEventLiveData

    fun setInitialText() {
        emailInputLiveData.value = "zelinskiypavlo@gmail.com"
        passwordInputLiveData.value = "password1"
    }

    fun loginUser() {
        launchRequest {
            val loginStatus = authRepository.signIn(
                email = emailInputLiveData.value!!,
                password = passwordInputLiveData.value!!
            )
            if (loginStatus)
                _loginEventLiveData.postValue(Event(loginStatus))
        }
    }

}