package com.test.thecocktaildb.dataNew.local.source

import androidx.lifecycle.MutableLiveData

interface AppSettingLocalSource {

    val showNavigationTitleLiveData: MutableLiveData<Boolean>
}