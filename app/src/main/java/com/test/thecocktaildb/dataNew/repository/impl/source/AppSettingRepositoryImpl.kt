package com.test.thecocktaildb.dataNew.repository.impl.source

import com.test.thecocktaildb.dataNew.db.source.AppSettingLocalSource
import com.test.thecocktaildb.dataNew.repository.source.AppSettingRepository
import javax.inject.Inject

class AppSettingRepositoryImpl @Inject constructor(localSource: AppSettingLocalSource) :
    AppSettingRepository {

    override val showNavigationTitleLiveData = localSource.showNavigationTitleLiveData

}
