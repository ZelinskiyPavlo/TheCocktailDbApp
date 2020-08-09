package com.test.thecocktaildb.util

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.test.thecocktaildb.data.CocktailsRepository
import com.test.thecocktaildb.ui.base.BaseViewModel
import com.test.thecocktaildb.ui.cocktail.MainViewModel
import com.test.thecocktaildb.ui.cocktail.host.HostViewModel
import com.test.thecocktaildb.ui.cocktail.host.SharedHostViewModel
import com.test.thecocktaildb.ui.detail.CocktailDetailsViewModel
import com.test.thecocktaildb.ui.search.SearchCocktailViewModel
import javax.inject.Inject

class GenericSavedStateViewModelFactory<out V : BaseViewModel>(
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

class SharedHostViewModelFactory @Inject constructor(
    private val repository: CocktailsRepository
) : ViewModelAssistedFactory<SharedHostViewModel> {
    override fun create(handle: SavedStateHandle): SharedHostViewModel {
        return SharedHostViewModel(handle, repository)
    }
}

class CocktailDetailsViewModelFactory @Inject constructor(
    private val repository: CocktailsRepository
) : ViewModelAssistedFactory<CocktailDetailsViewModel> {
    override fun create(handle: SavedStateHandle): CocktailDetailsViewModel {
        return CocktailDetailsViewModel(handle, repository)
    }
}

class HostViewModelFactory @Inject constructor(
    private val repository: CocktailsRepository
) : ViewModelAssistedFactory<HostViewModel> {
    override fun create(handle: SavedStateHandle): HostViewModel {
        return HostViewModel(handle, repository)
    }
}

class MainViewModelFactory @Inject constructor(
    private val repository: CocktailsRepository
) : ViewModelAssistedFactory<MainViewModel> {
    override fun create(handle: SavedStateHandle): MainViewModel {
        return MainViewModel(handle, repository)
    }
}

class SearchCocktailsViewModelFactory @Inject constructor(
    private val repository: CocktailsRepository
) : ViewModelAssistedFactory<SearchCocktailViewModel> {
    override fun create(handle: SavedStateHandle): SearchCocktailViewModel {
        return SearchCocktailViewModel(handle, repository)
    }
}
