package com.test.tabhost.ui

import androidx.lifecycle.SavedStateHandle
import com.test.presentation.ui.base.BaseViewModel
import com.test.repository.source.AppSettingRepository

class TabHostViewModel(
    savedStateHandle: SavedStateHandle,
    appSettingRepository: AppSettingRepository
) : BaseViewModel(savedStateHandle) {

    val shouldShowNavigationTitleFlow = appSettingRepository.observeShowNavigationTitle()
}