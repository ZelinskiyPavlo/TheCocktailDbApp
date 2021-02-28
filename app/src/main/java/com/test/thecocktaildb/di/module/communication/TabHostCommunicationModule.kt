package com.test.thecocktaildb.di.module.communication

import com.test.tabhost.api.TabHostCommunicationApi
import com.test.thecocktaildb.feature.tabhost.TabHostCommunicationImpl
import dagger.Binds
import dagger.Module

@Module
interface TabHostCommunicationModule {

    @Suppress("unused")
    @Binds
    fun bindTabHostCommunication(
        tabHostCommunicationImpl: TabHostCommunicationImpl
    ): TabHostCommunicationApi
}