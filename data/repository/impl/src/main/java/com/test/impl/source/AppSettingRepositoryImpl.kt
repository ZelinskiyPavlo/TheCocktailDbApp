package com.test.impl.source

import com.test.local.source.AppSettingLocalSource
import com.test.repository.source.AppSettingRepository
import javax.inject.Inject

class AppSettingRepositoryImpl @Inject constructor(localSource: AppSettingLocalSource) :
    AppSettingRepository {

    override val shouldShowNavigationTitleLiveData = localSource.showNavigationTitleLiveData

    override val currentLanguageLiveData = localSource.currentLanguageLiveData

}
