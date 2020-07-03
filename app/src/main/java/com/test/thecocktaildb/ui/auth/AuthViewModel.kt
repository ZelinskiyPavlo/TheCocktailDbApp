package com.test.thecocktaildb.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    private val login = "SomeLogin"
    private val password = "123456a"

    private var isDataCorrect = true

    val loginInputLiveData: MutableLiveData<String?> = MutableLiveData()
    val passwordInputLiveData: MutableLiveData<String?> = MutableLiveData()

    val isLoginDataValidLiveData: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        fun invalidateTypedData() {
            val typedLogin = loginInputLiveData.value
            val typedPassword = passwordInputLiveData.value

            if (typedLogin == null || typedPassword == null) {
                isDataCorrect = false
                return
            }

            if (typedLogin.length < 6) isDataCorrect = false

            if (typedPassword.length < 6) isDataCorrect = false

            if ((typedPassword.any { it.isDigit() } && typedPassword.any { it.isLetter() }).not())
                isDataCorrect = false

            isDataCorrect = (typedLogin == login && typedPassword == password)

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