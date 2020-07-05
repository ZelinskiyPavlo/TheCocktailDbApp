package com.test.thecocktaildb.ui.cocktailsScreen.favoriteScreen

import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.data.AppCocktailsRepository
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel()