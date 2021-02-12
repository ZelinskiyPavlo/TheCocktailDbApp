package com.test.thecocktaildb.di.module.navigation.feature

import com.test.auth.navigation.outer.AuthNavigationApi
import com.test.thecocktaildb.navigation.feature.AuthNavigationImpl
import dagger.Binds
import dagger.Module

@Module
interface AuthNavigationModule {

    @Binds
    fun bindAuthNavigationImpl(authHostNavigationImpl: AuthNavigationImpl): AuthNavigationApi
}