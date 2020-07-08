package com.test.thecocktaildb.ui.cocktailScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.test.thecocktaildb.ui.base.BaseViewModel

class SharedMainViewModel (stateHandle: SavedStateHandle) : BaseViewModel(stateHandle) {

    val isCheckBoxCheckedLiveData = MutableLiveData<Boolean>().apply { value = true }

}