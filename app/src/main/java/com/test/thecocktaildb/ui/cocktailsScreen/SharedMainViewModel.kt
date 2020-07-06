package com.test.thecocktaildb.ui.cocktailsScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SharedMainViewModel @Inject constructor(): ViewModel() {

    val isCheckBoxCheckedLiveData = MutableLiveData<Boolean>().apply { value = true }

}