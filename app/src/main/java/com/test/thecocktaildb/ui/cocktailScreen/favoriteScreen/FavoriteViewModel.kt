package com.test.thecocktaildb.ui.cocktailScreen.favoriteScreen

import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.data.AppCocktailsRepository
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel()