package com.test.thecocktaildb.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.util.Event

class AuthViewModel : ViewModel() {

    private var isDataCorrect = true

    val loginInputLiveData: MutableLiveData<String?> = MutableLiveData()
    val passwordInputLiveData: MutableLiveData<String?> = MutableLiveData()

    private val _errorLoginTextColorLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorLoginTextColorLiveData: LiveData<Boolean> = _errorLoginTextColorLiveData

    private val _errorPasswordTextColorLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorPasswordTextColorLiveData: LiveData<Boolean> = _errorPasswordTextColorLiveData

    private val _clearErrorTextColorEventLiveData: MutableLiveData<Event<Unit>> = MutableLiveData()
    val clearErrorTextColorEventLiveData: LiveData<Event<Unit>> = _clearErrorTextColorEventLiveData

    val isLoginDataValidLiveData: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        fun invalidateTypedData() {
            val typedLogin = loginInputLiveData.value
            val typedPassword = passwordInputLiveData.value

            _clearErrorTextColorEventLiveData.value = Event(Unit)
            _errorLoginTextColorLiveData.value = false
            _errorPasswordTextColorLiveData.value = false
            isDataCorrect = true

            if (typedLogin == null || typedPassword == null) {
                isDataCorrect = false
                return
            }

            if (typedLogin.length < 6) {
                isDataCorrect = false
                _errorLoginTextColorLiveData.value = true
            }

            if (typedPassword.length < 6) {
                isDataCorrect = false
                _errorPasswordTextColorLiveData.value = true
            }

            if ((typedPassword.any { it.isDigit() } && typedPassword.any { it.isLetter() }).not()){
                isDataCorrect = false
                _errorPasswordTextColorLiveData.value = true
            }

            value = isDataCorrect
        }

        addSource(loginInputLiveData) {
            invalidateTypedData()
        }
        addSource(passwordInputLiveData) {
            invalidateTypedData()
        }
    }

    fun setInitialText() {
        loginInputLiveData.value = "SomeLogin"
        passwordInputLiveData.value = "123456a"
    }
}