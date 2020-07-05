package com.test.thecocktaildb.ui.cocktailsScreen

import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.data.AppCocktailsRepository
import javax.inject.Inject

class CocktailsViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel()