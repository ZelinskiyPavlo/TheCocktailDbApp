package com.test.thecocktaildb.ui.auth.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.test.thecocktaildb.dataNew.repository.source.AuthRepository
import com.test.thecocktaildb.dataNew.repository.source.UserRepository
import com.test.thecocktaildb.ui.base.BaseViewModel
import com.test.thecocktaildb.util.Event

class SplashViewModel(
    savedStateHandle: SavedStateHandle,
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository
) : BaseViewModel(savedStateHandle) {

    private val _loginStatusEventLiveData = MutableLiveData<Event<Boolean>>()
    val loginStatusEventLiveData: LiveData<Event<Boolean>> = _loginStatusEventLiveData

//    fun checkLoginStatus() {
//        launchRequest {
//            userRepo.getUser()?.let {
//                val loginStatus = authRepo.signIn(
//                    email = "zelinskiypavlo@gmail.com",
//                    password = "password1"
//                )
//                if (loginStatus)
//                    _loginStatusEventLiveData.postValue(Event(loginStatus))
//            }
//            // TODO: 01.08.2020 перевірити чи воно все добре виведе, чи не виведе воно спочатку друге, а потім перше
//
//        }
//    }

    fun checkLoginStatus() {
//        _loginStatusEventLiveData.value = Event(false)
        _loginStatusEventLiveData.value = Event(true)
    }
}