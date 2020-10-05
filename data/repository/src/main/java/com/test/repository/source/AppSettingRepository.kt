package com.test.repository.source

import androidx.lifecycle.MutableLiveData

interface AppSettingRepository {

    val showNavigationTitleLiveData: MutableLiveData<Boolean>

}