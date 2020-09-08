package com.test.thecocktaildb.ui.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

open class BaseViewModel(val savedStateHandle: SavedStateHandle) : ViewModel()