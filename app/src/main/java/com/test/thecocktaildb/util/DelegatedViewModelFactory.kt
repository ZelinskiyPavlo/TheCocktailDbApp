package com.test.thecocktaildb.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.ui.cocktailsScreen.MainViewModel
import javax.inject.Inject

class DelegatedViewModelFactory @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
//            modelClass.name == "com.test.thecocktaildb.ui.cocktailsScreen.SharedMainViewModel" ->
//                SharedMainViewModel(this.repository) as T
            modelClass.name == "com.test.thecocktaildb.ui.cocktailsScreen.MainViewModel" ->
                MainViewModel(this.repository) as T
//            modelClass.isAssignableFrom(ProfileFragment::class.java) ->
//                SharedMainViewModel(this.repository) as T
//            modelClass.isAssignableFrom(ViewModelProvider::class.java) ->
//                SharedMainViewModel(this.repository) as T

            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}