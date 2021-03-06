package com.test.auth.di.navigation

import com.test.auth.navigation.inner.RegisterNavigationImpl
import com.test.dagger.scope.PerNestedFragment
import com.test.register.api.RegisterNavigationApi
import dagger.Binds
import dagger.Module

@Module
internal interface RegisterNavigationModule {

    @Suppress("unused")
    @Binds
    @PerNestedFragment
    fun bindRegisterNavigation(registerNavigationImpl: RegisterNavigationImpl): RegisterNavigationApi
}