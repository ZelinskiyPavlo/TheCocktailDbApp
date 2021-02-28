package com.test.thecocktaildb.ui

import androidx.lifecycle.SavedStateHandle
import com.test.presentation.ui.base.BaseViewModel
import com.test.repository.source.UserRepository

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
): BaseViewModel(savedStateHandle) {

    val userLiveData = userRepository.userLiveData

    var isUserLoggedIn = false

    init {
        userLiveData.observeForever { isUserLoggedIn = it != null }
    }

    fun refreshUser() {
        launchRequest {
            if (userRepository.hasUser()) userRepository.refreshUser()
        }
    }
}