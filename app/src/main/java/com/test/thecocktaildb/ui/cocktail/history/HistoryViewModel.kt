package com.test.thecocktaildb.ui.cocktail.history

import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.data.AppCocktailsRepository
import javax.inject.Inject

class HistoryViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel()