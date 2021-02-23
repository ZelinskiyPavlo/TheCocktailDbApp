package com.test.register.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.test.common.Event
import com.test.presentation.ui.base.BaseViewModel
import com.test.repository.source.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterViewModel(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository
) : BaseViewModel(savedStateHandle) {

    val emailInputLiveData: MutableLiveData<String?> = MutableLiveData()
    val nameInputLiveData: MutableLiveData<String?> = MutableLiveData()
    val lastNameInputLiveData: MutableLiveData<String?> = MutableLiveData()
    val passwordInputLiveData: MutableLiveData<String?> = MutableLiveData()
    val confirmPasswordInputLiveData: MutableLiveData<String?> = MutableLiveData()

    var wrongEmail = false
    var wrongName = false
    var wrongLastName = false
    var wrongPassword = false
    var wrongPasswordConfirm = false

    private val _clearErrorTextColorEventLiveData: MutableLiveData<Event<Unit>> = MutableLiveData()
    val clearErrorTextColorEventLiveData: LiveData<Event<Unit>> = _clearErrorTextColorEventLiveData

    private var isDataCorrect = true

    val isDataValidLiveData: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        fun invalidateTypedData() {
            val typedEmail = emailInputLiveData.value ?: ""
            val typedName = nameInputLiveData.value ?: ""
            val typedLastName = lastNameInputLiveData.value ?: ""
            val typedPassword = passwordInputLiveData.value ?: ""
            val typedConfirmPassword = confirmPasswordInputLiveData.value ?: ""

            _clearErrorTextColorEventLiveData.value = Event(Unit)
            isDataCorrect = true
            wrongEmail = false
            wrongName = false
            wrongLastName = false
            wrongPassword = false
            wrongPasswordConfirm = false

            if (typedEmail.length < 6) {
                isDataCorrect = false
                wrongEmail = true
            }
            if (typedName.length < 4) {
                isDataCorrect = false
                wrongName = true
            }
            if (typedLastName.length < 4) {
                isDataCorrect = false
                wrongLastName = true
            }
            if (typedPassword.length < 6) {
                isDataCorrect = false
                wrongPassword = true
            }
            if (typedConfirmPassword.length < 6) {
                isDataCorrect = false
                wrongPasswordConfirm = true
            }

            if ((typedPassword.any { it.isDigit() }
                        && typedPassword.any { it.isLetter() }).not()) {
                isDataCorrect = false
                wrongPassword = true
            }

            if ((typedConfirmPassword.any { it.isDigit() }
                        && typedConfirmPassword.any { it.isLetter() }).not()) {
                isDataCorrect = false
                wrongPasswordConfirm = true
            }

            value = isDataCorrect
        }

        addSource(emailInputLiveData) { invalidateTypedData() }

        addSource(nameInputLiveData) { invalidateTypedData() }

        addSource(lastNameInputLiveData) { invalidateTypedData() }

        addSource(passwordInputLiveData) { invalidateTypedData() }

        addSource(confirmPasswordInputLiveData) { invalidateTypedData() }
    }

    private val _registerEventLiveData: MutableLiveData<Event<Unit>> = MutableLiveData()
    val registerEventLiveData: LiveData<Event<Unit>> = _registerEventLiveData

    fun registerUser() {
        launchRequest {
            authRepository.signUp(
                email = emailInputLiveData.value!!,
                name = nameInputLiveData.value!!,
                lastName = lastNameInputLiveData.value!!,
                password = passwordInputLiveData.value!!
            )
            withContext(Dispatchers.Main) {
                _registerEventLiveData.value = Event(Unit)
            }
        }
    }

}