package com.test.auth.di.navigation

import com.test.auth.navigation.inner.LoginNavigationImpl
import com.test.dagger.scope.PerNestedFragment
import com.test.login.navigation.LoginNavigationApi
import dagger.Binds
import dagger.Module

@Module
interface LoginNavigationModule {

    @Binds
    @PerNestedFragment
    fun bindLoginNavigation(loginNavigationImpl: LoginNavigationImpl): LoginNavigationApi
}