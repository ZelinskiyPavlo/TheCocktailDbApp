package com.test.thecocktaildb.ui.cocktail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.test.thecocktaildb.ui.base.BaseViewModel

class SharedMainViewModel (stateHandle: SavedStateHandle) : BaseViewModel(stateHandle) {

    val shouldShowMainNavigationTitlesLiveData = MutableLiveData<Boolean>().apply { value = true }

}