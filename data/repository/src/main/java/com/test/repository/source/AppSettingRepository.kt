package com.test.repository.source

import androidx.lifecycle.MutableLiveData

interface AppSettingRepository {

    val shouldShowNavigationTitleLiveData: MutableLiveData<Boolean>

    val currentLanguageLiveData: MutableLiveData<Int>
}