package com.test.login.factory

import androidx.lifecycle.SavedStateHandle
import com.test.login.api.LoginNavigationApi
import com.test.login.ui.LoginViewModel
import com.test.presentation.factory.ViewModelAssistedFactory
import com.test.repository.source.AuthRepository
import javax.inject.Inject

internal class LoginViewModelFactory @Inject constructor(
    private val authRepository: AuthRepository,
    private val navigator: LoginNavigationApi
) : ViewModelAssistedFactory<LoginViewModel> {
    override fun create(handle: SavedStateHandle): LoginViewModel {
        return LoginViewModel(handle, authRepository, navigator)
    }
}