package com.test.cocktail.ui

import androidx.lifecycle.SavedStateHandle
import com.test.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

class CommunicationViewModel(savedStateHandle: SavedStateHandle): BaseViewModel(savedStateHandle) {

    val onNestedFragmentNavigationFlow = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
}