package com.test.auth.di

import com.test.auth.ui.AuthActivity
import com.test.dagger.scope.PerFeature
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ContributeScreenModule {

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeAuthActivity(): AuthActivity

//
//    @ContributesAndroidInjector
//    @Suppress("unused")
//    abstract fun contributeLoginFragment(): LoginFragment
//
//    @ContributesAndroidInjector
//    @Suppress("unused")
//    abstract fun contributeRegisterFragment(): RegisterFragment
}