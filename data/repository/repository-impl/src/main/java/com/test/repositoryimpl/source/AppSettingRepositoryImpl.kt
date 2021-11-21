package com.test.repositoryimpl.source

import com.test.local.source.AppSettingLocalSource
import com.test.repository.source.AppSettingRepository
import javax.inject.Inject

class AppSettingRepositoryImpl @Inject constructor(private val localSource: AppSettingLocalSource) :
    AppSettingRepository {

    override fun observeShowNavigationTitle() = localSource.observeShowNavigationTitle()

    override var showNavigationTitle = localSource.showNavigationTitle

    override fun observeCurrentLanguage() = localSource.observeCurrentLanguage()

    override var currentLanguage = localSource.currentLanguage
}
