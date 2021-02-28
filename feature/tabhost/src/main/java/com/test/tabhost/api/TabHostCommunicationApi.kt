package com.test.tabhost.api

import androidx.lifecycle.LiveData
import com.test.common.Event

interface TabHostCommunicationApi {

    val cocktailWithIdNotFoundEvent: LiveData<Event<Unit>>
}