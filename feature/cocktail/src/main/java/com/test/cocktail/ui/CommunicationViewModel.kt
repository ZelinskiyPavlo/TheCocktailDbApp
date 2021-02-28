package com.test.cocktail.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.test.presentation.ui.base.BaseViewModel

class CommunicationViewModel(savedStateHandle: SavedStateHandle): BaseViewModel(savedStateHandle) {

    val onNestedFragmentNavigationLiveData = MutableLiveData<Boolean>()
}