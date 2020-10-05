package com.test.auth.ui

import androidx.lifecycle.SavedStateHandle
import com.test.presentation.ui.base.BaseViewModel

class AuthViewModel(savedStateHandle: SavedStateHandle) : BaseViewModel(savedStateHandle) {

    var firebaseData: Pair<String?, String?> = Pair(null, null)
}