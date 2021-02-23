package com.test.auth.di.navigation

import com.test.auth.navigation.inner.RegisterNavigationImpl
import com.test.dagger.scope.PerNestedFragment
import com.test.register.api.RegisterNavigationApi
import dagger.Binds
import dagger.Module

@Module
interface RegisterNavigationModule {

    @Binds
    @PerNestedFragment
    fun bindRegisterNavigation(registerNavigationImpl: RegisterNavigationImpl): RegisterNavigationApi
}