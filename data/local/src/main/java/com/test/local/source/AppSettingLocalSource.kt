package com.test.local.source

import androidx.lifecycle.MutableLiveData

interface AppSettingLocalSource {

    val showNavigationTitleLiveData: MutableLiveData<Boolean>

    val currentLanguageLiveData: MutableLiveData<Int>
}