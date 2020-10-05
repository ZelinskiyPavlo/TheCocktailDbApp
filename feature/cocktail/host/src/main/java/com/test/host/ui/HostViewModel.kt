package com.test.host.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import com.test.thecocktaildb.dataBinding.adapter.Page
import com.test.thecocktaildb.presentation.ui.base.BaseViewModel
import com.test.thecocktaildb.util.liveDataStateHandle

class HostViewModel(stateHandle: SavedStateHandle) : BaseViewModel(stateHandle) {

    val currentPageLiveData by liveDataStateHandle(initialValue = Page.HistoryPage)

    val viewPagerLiveData = MediatorLiveData<Unit>().apply {

        addSource(currentPageLiveData) {
            value = Unit
        }
    }
}