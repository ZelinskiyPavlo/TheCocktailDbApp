package com.test.thecocktaildb.di.module.navigation.feature

import com.test.auth.api.AuthNavigationApi
import com.test.thecocktaildb.feature.auth.AuthNavigationImpl
import dagger.Binds
import dagger.Module

@Module
interface AuthNavigationModule {

    @Binds
    fun bindAuthNavigationImpl(authHostNavigationImpl: AuthNavigationImpl): AuthNavigationApi
}