package com.test.profile.ui

import androidx.lifecycle.*
import com.test.common.Event
import com.test.presentation.extension.distinctNotNullValues
import com.test.presentation.mapper.user.UserModelMapper
import com.test.presentation.model.user.UserModel
import com.test.presentation.ui.base.BaseViewModel
import com.test.repository.source.TokenRepository
import com.test.repository.source.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ProfileViewModel(
    savedStateHandle: SavedStateHandle,
    private val tokenRepo: TokenRepository,
    private val userRepo: UserRepository,
    private val userMapper: UserModelMapper
) : BaseViewModel(savedStateHandle) {

    private val userModelLiveData: LiveData<UserModel?> = userRepo.userLiveData.map {
        it?.run(userMapper::mapTo)
    }

    private val _userNameLiveData = userModelLiveData.map { it?.name }
    val userNameLiveData: LiveData<String?> = _userNameLiveData

    private val _userLastNameLiveData = userModelLiveData.map { it?.lastName }
    val userLastNameLiveData: LiveData<String?> = _userLastNameLiveData

    private val _userEmailLiveData = userModelLiveData.map { it?.email }
    val userEmailLiveData: LiveData<String?> = _userEmailLiveData

    val userFullNameLiveData = MediatorLiveData<String>().apply {
        fun generateFullName() {
            value = userNameLiveData.value?.capitalize()
                .plus(" ")
                .plus(userLastNameLiveData.value?.capitalize())
        }

        addSource(userNameLiveData) {
            generateFullName()
        }
        addSource(userLastNameLiveData) {
            generateFullName()
        }
    }
    val userDataChangedLiveData = userFullNameLiveData.distinctNotNullValues()

    val userAvatarLiveData = userModelLiveData.map { it?.avatar }.distinctUntilChanged()

    val nameInputLiveData = MutableLiveData<String>()
    val lastNameInputLiveData = MutableLiveData<String>()
    val emailInputLiveData = MutableLiveData<String>()

    private val _logOutUserEventLiveData = MutableLiveData<Event<Unit>>()
    val logOutUserEventLiveData: LiveData<Event<Unit>> = _logOutUserEventLiveData

    private val mockUpdateAvatarResponseLiveData = MutableLiveData<String>()
    private val mockUpdateAvatarObserver = Observer<String> { avatarUrl ->
        launchRequest {
            val updatedUser = UserModel(
                email = emailInputLiveData.value!!,
                name = nameInputLiveData.value!!,
                lastName = lastNameInputLiveData.value!!,
                avatar = avatarUrl
            )
            userRepo.updateUser(updatedUser.run(userMapper::mapFrom))
        }
    }

    init {
        mockUpdateAvatarResponseLiveData.observeForever(mockUpdateAvatarObserver)
    }

    override fun onCleared() {
        mockUpdateAvatarResponseLiveData.removeObserver(mockUpdateAvatarObserver)
        super.onCleared()
    }

    fun isUserDataChanged(): Boolean {
        if (userNameLiveData.value == nameInputLiveData.value
            && userLastNameLiveData.value == lastNameInputLiveData.value
            && userEmailLiveData.value == emailInputLiveData.value
        ) return false
        return true
    }

    fun updateUser() {
        launchRequest {
            val updatedUser = UserModel(
                email = emailInputLiveData.value!!,
                name = nameInputLiveData.value!!,
                lastName = lastNameInputLiveData.value!!,
                avatar = userAvatarLiveData.value
            )
            userRepo.updateUser(updatedUser.run(userMapper::mapFrom))
        }
    }

    fun uploadAvatar(avatar: File, onUploadProgress: (Float) -> Unit = { _ -> }) {
        launchRequest(mockUpdateAvatarResponseLiveData) {
            userRepo.updateUserAvatar(avatar, onUploadProgress)
        }
    }

    fun isInputDataInvalid(): Boolean {
        val typedEmail = emailInputLiveData.value ?: ""
        val typedName = nameInputLiveData.value ?: ""
        val typedLastName = lastNameInputLiveData.value ?: ""

        if (typedEmail.length < 6) {
            return true
        }
        if (typedName.length < 4) {
            return true
        }
        if (typedLastName.length < 4) {
            return true
        }
        return false
    }

    fun logOutUser() {
        launchRequest {
            tokenRepo.authToken = null
            userRepo.deleteUser()
            withContext(Dispatchers.Main) {
                _logOutUserEventLiveData.value = Event(Unit)
            }
        }
    }
}