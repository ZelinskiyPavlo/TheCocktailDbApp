package com.test.thecocktaildb.di.module.communication

import com.test.detail.api.DetailCommunicationApi
import com.test.thecocktaildb.feature.detail.DetailCommunicationImpl
import dagger.Binds
import dagger.Module

@Module
interface DetailCommunicationModule {

    @Suppress("unused")
    @Binds
    fun bindDetailCommunication(detailCommunicationImpl: DetailCommunicationImpl): DetailCommunicationApi
}