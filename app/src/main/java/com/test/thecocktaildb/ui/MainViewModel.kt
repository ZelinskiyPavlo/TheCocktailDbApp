package com.test.thecocktaildb.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.test.presentation.ui.base.BaseViewModel
import com.test.presentation.util.WhileViewSubscribed
import com.test.repository.source.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
) : BaseViewModel(savedStateHandle) {

    sealed class Event {
        object InitNavigation : Event()
    }

    private val _eventsChannel = Channel<Event>(capacity = Channel.CONFLATED)
    val eventsFlow = _eventsChannel.receiveAsFlow()

    private val userFlow = userRepository.userFlow

    var isUserLoggedInFlow = userFlow.map { it != null }
        .stateIn(viewModelScope, WhileViewSubscribed, false)

    init {
        refreshUser()
        isUserLoggedInFlow.launchIn(viewModelScope)
    }

    fun handleColdStart() {
        viewModelScope.launch {
            userFlow.first()
            _eventsChannel.trySend(Event.InitNavigation)
        }
    }

    private fun refreshUser() {
        launchRequest {
            if (userRepository.hasUser()) userRepository.refreshUser()
        }
    }
}