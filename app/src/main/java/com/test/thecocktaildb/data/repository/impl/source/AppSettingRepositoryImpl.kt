package com.test.thecocktaildb.data.repository.impl.source

import com.test.thecocktaildb.data.local.source.AppSettingLocalSource
import com.test.thecocktaildb.data.repository.source.AppSettingRepository
import javax.inject.Inject

class AppSettingRepositoryImpl @Inject constructor(localSource: AppSettingLocalSource) :
    AppSettingRepository {

    override val showNavigationTitleLiveData = localSource.showNavigationTitleLiveData

}
