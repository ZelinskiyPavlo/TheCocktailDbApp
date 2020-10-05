package com.test.splash.factory

import androidx.lifecycle.SavedStateHandle
import com.test.presentation.factory.ViewModelAssistedFactory
import com.test.repository.source.UserRepository
import com.test.splash.ui.SplashViewModel
import javax.inject.Inject

class SplashViewModelFactory @Inject constructor(
    private val userRepo: UserRepository
) : ViewModelAssistedFactory<SplashViewModel> {
    override fun create(handle: SavedStateHandle): SplashViewModel {
        return SplashViewModel(handle, userRepo)
    }
}