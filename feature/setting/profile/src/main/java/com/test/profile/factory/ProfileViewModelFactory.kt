package com.test.profile.factory

import androidx.lifecycle.SavedStateHandle
import com.test.presentation.factory.ViewModelAssistedFactory
import com.test.presentation.mapper.user.UserModelMapper
import com.test.profile.ui.ProfileViewModel
import com.test.repository.source.TokenRepository
import com.test.repository.source.UserRepository
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(
    private val tokenRepo: TokenRepository,
    private val userRepo: UserRepository,
    private val userMapper: UserModelMapper
) : ViewModelAssistedFactory<ProfileViewModel> {
    override fun create(handle: SavedStateHandle): ProfileViewModel {
        return ProfileViewModel(handle, tokenRepo, userRepo, userMapper)
    }
}
