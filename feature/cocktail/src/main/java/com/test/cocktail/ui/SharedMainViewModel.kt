package com.test.cocktail.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.test.thecocktaildb.presentation.ui.base.BaseViewModel

class SharedMainViewModel(stateHandle: SavedStateHandle) : BaseViewModel(stateHandle) {

    val shouldShowMainNavigationTitlesLiveData = MutableLiveData<Boolean>().apply { value = true }

    private val _showNavTitlesViewVisibilityLiveData = MutableLiveData<Boolean>()
    val showNavTitlesViewVisibilityLiveData: LiveData<Boolean> =
        _showNavTitlesViewVisibilityLiveData

    fun getDataFromRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        val isShowNavTitlesViewShown = remoteConfig["enable_show_nav_titles_checkbox"].asBoolean()
        _showNavTitlesViewVisibilityLiveData.value = isShowNavTitlesViewShown

        val isNavTitlesShown = remoteConfig["change_visibility_of_nav_titles"].asBoolean()

        if (!isShowNavTitlesViewShown)
            shouldShowMainNavigationTitlesLiveData.value = isNavTitlesShown
    }

}