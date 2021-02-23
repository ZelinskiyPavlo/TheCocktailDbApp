package com.test.thecocktaildb.di.module.navigation.feature

import com.test.search.api.SearchNavigationApi
import com.test.thecocktaildb.feature.search.SearchNavigationImpl
import dagger.Binds
import dagger.Module

@Module
interface SearchNavigationModule {

    @Binds
    fun bindSearchNavigation(searchNavigationImpl: SearchNavigationImpl): SearchNavigationApi
}