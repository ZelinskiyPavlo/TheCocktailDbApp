package com.test.thecocktaildb.factory

import androidx.lifecycle.SavedStateHandle
import com.test.presentation.factory.ViewModelAssistedFactory
import com.test.repository.source.UserRepository
import com.test.thecocktaildb.ui.MainViewModel
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(
    private val userRepository: UserRepository
) : ViewModelAssistedFactory<MainViewModel> {
    override fun create(handle: SavedStateHandle): MainViewModel {
        return MainViewModel(handle, userRepository)
    }
}