package com.test.thecocktaildb.presentation.ui.cocktail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.test.thecocktaildb.presentation.ui.base.BaseViewModel

class SharedMainViewModel (stateHandle: SavedStateHandle) : BaseViewModel(stateHandle) {

    val shouldShowMainNavigationTitlesLiveData = MutableLiveData<Boolean>().apply { value = true }

}