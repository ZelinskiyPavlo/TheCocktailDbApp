package com.test.thecocktaildb.data.repository.source

import androidx.lifecycle.MutableLiveData

interface AppSettingRepository {

    val showNavigationTitleLiveData: MutableLiveData<Boolean>

}