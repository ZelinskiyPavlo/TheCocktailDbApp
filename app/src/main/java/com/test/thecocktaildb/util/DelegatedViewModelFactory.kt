package com.test.thecocktaildb.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.ui.cocktailsScreen.MainViewModel
import com.test.thecocktaildb.ui.cocktailsScreen.fragmentHostScreen.SharedHostViewModel
import javax.inject.Inject

// TODO: напевно її треба зробити як Singleton (але перевірити чи все працює)
class DelegatedViewModelFactory @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass.name) {
            "com.test.thecocktaildb.ui.cocktailsScreen.fragmentHostScreen.SharedHostViewModel" ->
                SharedHostViewModel(this.repository) as T
            "com.test.thecocktaildb.ui.cocktailsScreen.MainViewModel" ->
                MainViewModel(this.repository) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}