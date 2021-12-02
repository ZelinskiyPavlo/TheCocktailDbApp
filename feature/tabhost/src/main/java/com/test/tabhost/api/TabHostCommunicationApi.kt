package com.test.tabhost.api

import kotlinx.coroutines.flow.Flow

interface TabHostCommunicationApi {

    val cocktailWithIdNotFoundFlow: Flow<Unit>
}