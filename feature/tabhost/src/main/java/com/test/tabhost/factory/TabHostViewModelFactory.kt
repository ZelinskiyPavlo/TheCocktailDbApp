package com.test.tabhost.factory

import androidx.lifecycle.SavedStateHandle
import com.test.presentation.factory.ViewModelAssistedFactory
import com.test.repository.source.AppSettingRepository
import com.test.tabhost.ui.TabHostViewModel
import javax.inject.Inject

internal class TabHostViewModelFactory @Inject constructor(
    private val appSettingRepository: AppSettingRepository
) : ViewModelAssistedFactory<TabHostViewModel> {
    override fun create(handle: SavedStateHandle): TabHostViewModel {
        return TabHostViewModel(handle, appSettingRepository)
    }
}