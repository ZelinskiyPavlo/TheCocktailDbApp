package com.test.thecocktaildb.di.module.navigation.feature

import com.test.search.navigation.SearchNavigationApi
import com.test.thecocktaildb.navigation.feature.SearchNavigationImpl
import dagger.Binds
import dagger.Module

@Module
interface SearchNavigationModule {

    @Binds
    fun bindSearchNavigation(searchNavigationImpl: SearchNavigationImpl): SearchNavigationApi
}