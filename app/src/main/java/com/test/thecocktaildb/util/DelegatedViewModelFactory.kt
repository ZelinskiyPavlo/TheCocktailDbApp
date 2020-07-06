package com.test.thecocktaildb.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.ui.cocktailScreen.MainViewModel
import com.test.thecocktaildb.ui.cocktailScreen.fragmentHostScreen.SharedHostViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DelegatedViewModelFactory @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass.name) {
            "com.test.thecocktaildb.ui.cocktailScreen.fragmentHostScreen.SharedHostViewModel" ->
                SharedHostViewModel(this.repository) as T
            "com.test.thecocktaildb.ui.cocktailScreen.MainViewModel" ->
                MainViewModel(this.repository) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}