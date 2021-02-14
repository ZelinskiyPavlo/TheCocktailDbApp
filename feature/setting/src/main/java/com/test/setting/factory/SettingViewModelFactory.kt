package com.test.setting.factory

import androidx.lifecycle.SavedStateHandle
import com.test.presentation.factory.ViewModelAssistedFactory
import com.test.repository.source.AppSettingRepository
import com.test.setting.ui.SettingViewModel
import javax.inject.Inject

class SettingViewModelFactory @Inject constructor(
    private val settingRepository: AppSettingRepository
) : ViewModelAssistedFactory<SettingViewModel> {
    override fun create(handle: SavedStateHandle): SettingViewModel {
        return SettingViewModel(handle, settingRepository)
    }
}