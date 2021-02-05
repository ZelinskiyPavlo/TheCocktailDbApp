package com.test.thecocktaildb.di.module.navigation

import com.test.auth.navigation.outer.AuthNavigationApi
import com.test.thecocktaildb.navigation.AuthNavigationImpl
import dagger.Binds
import dagger.Module

@Module
interface AuthNavigationModule {

    @Binds
    fun bindAuthNavigationImpl(authHostNavigationImpl: AuthNavigationImpl): AuthNavigationApi
}