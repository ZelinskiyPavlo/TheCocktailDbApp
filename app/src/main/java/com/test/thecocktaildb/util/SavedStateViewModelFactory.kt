package com.test.thecocktaildb.util

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.test.thecocktaildb.dataNew.repository.source.AuthRepository
import com.test.thecocktaildb.dataNew.repository.source.CocktailRepository
import com.test.thecocktaildb.dataNew.repository.source.UserRepository
import com.test.thecocktaildb.presentationNew.mapper.CocktailModelMapper
import com.test.thecocktaildb.ui.auth.login.LoginViewModel
import com.test.thecocktaildb.ui.auth.register.RegisterViewModel
import com.test.thecocktaildb.ui.auth.splash.SplashViewModel
import com.test.thecocktaildb.ui.base.BaseViewModel
import com.test.thecocktaildb.ui.cocktailDetailsScreen.CocktailDetailsViewModel
import com.test.thecocktaildb.ui.cocktailScreen.MainViewModel
import com.test.thecocktaildb.ui.cocktailScreen.fragmentHostScreen.HostViewModel
import com.test.thecocktaildb.ui.cocktailScreen.fragmentHostScreen.SharedHostViewModel
import com.test.thecocktaildb.ui.searchCocktailsScreen.SearchCocktailsViewModel
import javax.inject.Inject

class SavedStateViewModelFactory<out V : BaseViewModel>(
    private val viewModelFactory: ViewModelAssistedFactory<V>,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String, modelClass: Class<T>, handle: SavedStateHandle
    ): T {
        return viewModelFactory.create(handle) as T
    }
}

interface ViewModelAssistedFactory<T : BaseViewModel> {
    fun create(handle: SavedStateHandle): T
}

class SplashViewModelFactory @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepo: UserRepository
) : ViewModelAssistedFactory<SplashViewModel> {
    override fun create(handle: SavedStateHandle): SplashViewModel {
        return SplashViewModel(handle, authRepository, userRepo)
    }
}

class LoginViewModelFactory @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModelAssistedFactory<LoginViewModel> {
    override fun create(handle: SavedStateHandle): LoginViewModel {
        return LoginViewModel(handle, authRepository)
    }
}

class RegisterViewModelFactory @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModelAssistedFactory<RegisterViewModel> {
    override fun create(handle: SavedStateHandle): RegisterViewModel {
        return RegisterViewModel(handle, authRepository)
    }
}

class SharedHostViewModelFactory @Inject constructor(
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper
) : ViewModelAssistedFactory<SharedHostViewModel> {
    override fun create(handle: SavedStateHandle): SharedHostViewModel {
        return SharedHostViewModel(handle, cocktailRepo, cocktailMapper)
    }
}

class CocktailDetailsViewModelFactory @Inject constructor(
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper,
) : ViewModelAssistedFactory<CocktailDetailsViewModel> {
    override fun create(handle: SavedStateHandle): CocktailDetailsViewModel {
        return CocktailDetailsViewModel(handle, cocktailRepo, cocktailMapper)
    }
}

class HostViewModelFactory @Inject constructor() : ViewModelAssistedFactory<HostViewModel> {
    override fun create(handle: SavedStateHandle): HostViewModel {
        return HostViewModel(handle)
    }
}

class MainViewModelFactory @Inject constructor(
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper,
) : ViewModelAssistedFactory<MainViewModel> {
    override fun create(handle: SavedStateHandle): MainViewModel {
        return MainViewModel(handle, cocktailRepo, cocktailMapper)
    }
}

class SearchCocktailsViewModelFactory @Inject constructor(
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper,
) : ViewModelAssistedFactory<SearchCocktailsViewModel> {
    override fun create(handle: SavedStateHandle): SearchCocktailsViewModel {
        return SearchCocktailsViewModel(handle, cocktailRepo, cocktailMapper)
    }
}
