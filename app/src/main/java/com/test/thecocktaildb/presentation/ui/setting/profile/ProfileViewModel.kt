package com.test.thecocktaildb.presentation.ui.setting.profile

import androidx.lifecycle.*
import com.test.thecocktaildb.data.repository.source.TokenRepository
import com.test.thecocktaildb.data.repository.source.UserRepository
import com.test.thecocktaildb.presentation.mapper.UserModelMapper
import com.test.thecocktaildb.presentation.model.UserModel
import com.test.thecocktaildb.presentation.ui.base.BaseViewModel
import com.test.thecocktaildb.util.Event
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

    val userAvatarLiveData = userModelLiveData.map { it?.avatar }.distinctUntilChanged()

    val nameInputLiveData = MutableLiveData<String>()
    val lastNameInputLiveData = MutableLiveData<String>()
    val emailInputLiveData = MutableLiveData<String>()

    private val _updateUserEventLiveData = MutableLiveData<Event<Unit>>()
    val updateUserEventLiveData: LiveData<Event<Unit>> = _updateUserEventLiveData

    private val _logOutUserEventLiveData = MutableLiveData<Event<Unit>>()
    val logOutUserEventLiveData: LiveData<Event<Unit>> = _logOutUserEventLiveData

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

            withContext(Dispatchers.Main) {
                _updateUserEventLiveData.value = Event(Unit)
            }
        }
    }

    fun uploadAvatar(avatar: File, onUploadProgress: (Float) -> Unit = { _ -> }) {
        launchRequest {
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
            tokenRepo.token = null
            userRepo.deleteUser()
            withContext(Dispatchers.Main) {
                _logOutUserEventLiveData.value = Event(Unit)
            }
        }
    }
}