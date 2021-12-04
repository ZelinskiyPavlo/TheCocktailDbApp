package com.test.register.factory

import androidx.lifecycle.SavedStateHandle
import com.test.presentation.factory.ViewModelAssistedFactory
import com.test.register.api.RegisterNavigationApi
import com.test.register.ui.RegisterViewModel
import com.test.repository.source.AuthRepository
import javax.inject.Inject

internal class RegisterViewModelFactory @Inject constructor(
    private val authRepository: AuthRepository,
    private val navigator: RegisterNavigationApi
) : ViewModelAssistedFactory<RegisterViewModel> {
    override fun create(handle: SavedStateHandle): RegisterViewModel {
        return RegisterViewModel(handle, authRepository, navigator)
    }
}
