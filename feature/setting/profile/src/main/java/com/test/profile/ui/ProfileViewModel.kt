package com.test.profile.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.test.presentation.mapper.user.UserModelMapper
import com.test.presentation.model.user.UserModel
import com.test.presentation.ui.base.BaseViewModel
import com.test.presentation.util.WhileViewSubscribed
import com.test.profile.analytic.ProfileAnalyticApi
import com.test.profile.api.ProfileNavigationApi
import com.test.repository.source.TokenRepository
import com.test.repository.source.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import java.io.File

class ProfileViewModel(
    savedStateHandle: SavedStateHandle,
    private val tokenRepo: TokenRepository,
    private val userRepo: UserRepository,
    private val userMapper: UserModelMapper,
    private val navigator: ProfileNavigationApi,
    private val analytic: ProfileAnalyticApi
) : BaseViewModel(savedStateHandle) {

    sealed class Event

    private val _eventsChannel = Channel<Event>(capacity = Channel.CONFLATED)
    val eventsFlow = _eventsChannel.receiveAsFlow()

    private val userModelFlow = userRepo.userFlow.map {
        it?.run(userMapper::mapTo)
    }

    val userNameFlow = userModelFlow.filterNotNull().map { it.name }
    .stateIn(viewModelScope, WhileViewSubscribed, "")

    val userLastNameFlow = userModelFlow.filterNotNull().map { it.lastName }
        .stateIn(viewModelScope, WhileViewSubscribed, "")

    val userEmailFlow = userModelFlow.filterNotNull().map { it.email }
        .stateIn(viewModelScope, WhileViewSubscribed, "")

    val userFullNameFlow = combine(userNameFlow, userLastNameFlow) { name, lastName ->
        name.replaceFirstChar { it.uppercase() }
            .plus(" ")
            .plus(lastName.replaceFirstChar { it.uppercase() })
    }.stateIn(viewModelScope, WhileViewSubscribed, "")

    val userDataChangedFlow = userFullNameFlow.filter { it.isEmpty() }.drop(1)
        .onEach { analytic.logUserNameChanged(it) }

    val userAvatarFlow = userModelFlow.filterNotNull().map { it.avatar }
        .stateIn(viewModelScope, WhileViewSubscribed, null)

    val nameInputFlow = MutableStateFlow("")
    val lastNameInputFlow = MutableStateFlow("")
    val emailInputFlow = MutableStateFlow("")

    fun isUserDataChanged(): Boolean {
        if (userNameFlow.value == nameInputFlow.value
            && userLastNameFlow.value == lastNameInputFlow.value
            && userEmailFlow.value == emailInputFlow.value
        ) return false
        return true
    }

    fun updateUser() {
        launchRequest {
            val updatedUser = UserModel(
                email = emailInputFlow.value,
                name = nameInputFlow.value,
                lastName = lastNameInputFlow.value,
                avatar = userAvatarFlow.value
            )
            userRepo.updateUser(updatedUser.run(userMapper::mapFrom))
        }
    }

    fun uploadAvatar(avatar: File, onUploadProgress: (Float) -> Unit = { _ -> }) {
        launchRequest {
            val newAvatarUrl = userRepo.updateUserAvatar(avatar, onUploadProgress)
            val updatedUser = UserModel(
                email = emailInputFlow.value,
                name = nameInputFlow.value,
                lastName = lastNameInputFlow.value,
                avatar = newAvatarUrl
            )
            userRepo.updateUser(updatedUser.run(userMapper::mapFrom))

            // TODO: 04.12.2021 Check how many times it called
            analytic.logUserAvatarChanged(newAvatarUrl, userFullNameFlow.value)
        }
    }

    fun isInputDataInvalid(): Boolean {
        if (emailInputFlow.value.length < 6) {
            return true
        }
        if (nameInputFlow.value.length < 4) {
            return true
        }
        if (lastNameInputFlow.value.length < 4) {
            return true
        }
        return false
    }

    fun logOutUser() {
        launchRequest {
            tokenRepo.authToken = ""
            userRepo.deleteUser()
            withContext(Dispatchers.Main) {
                navigator.logOut()
            }
        }
    }

    fun exit() {
        navigator.exit()
    }
}