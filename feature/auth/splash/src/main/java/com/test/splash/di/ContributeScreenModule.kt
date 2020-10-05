package com.test.splash.di

import com.test.splash.ui.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ContributeScreenModule {

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeSplashFragment(): SplashFragment
}