package com.test.thecocktaildb.ui.cocktail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.test.thecocktaildb.ui.base.BaseViewModel

// TODO: видалена в 8 гілці (версія з 9! гілки)
class SharedMainViewModel (stateHandle: SavedStateHandle) : BaseViewModel(stateHandle) {

    val isCheckBoxCheckedLiveData = MutableLiveData<Boolean>().apply { value = true }

}