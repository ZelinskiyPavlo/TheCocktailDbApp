package com.test.thecocktaildb.dataNew.db.source

import androidx.lifecycle.MutableLiveData

interface AppSettingLocalSource {

    val showNavigationTitleLiveData: MutableLiveData<Boolean>

}