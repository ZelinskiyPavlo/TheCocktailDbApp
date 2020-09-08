package com.test.thecocktaildb.dataNew.repository.source

import androidx.lifecycle.MutableLiveData

interface AppSettingRepository {

    val showNavigationTitleLiveData: MutableLiveData<Boolean>

}