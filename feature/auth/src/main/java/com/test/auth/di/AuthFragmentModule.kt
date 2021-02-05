package com.test.auth.di

import com.test.auth.di.navigation.LoginNavigationModule
import com.test.auth.di.navigation.RegisterNavigationModule
import com.test.dagger.scope.PerNestedFragment
import com.test.login.ui.LoginFragment
import com.test.register.ui.RegisterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AuthFragmentModule {
    @Suppress("unused")
    @PerNestedFragment
    @ContributesAndroidInjector(modules = [LoginNavigationModule::class])
    fun contributeAuthLoginFragment(): LoginFragment

    @Suppress("unused")
    @PerNestedFragment
    @ContributesAndroidInjector(modules = [RegisterNavigationModule::class])
    fun contributeAuthRegisterFragment(): RegisterFragment
}