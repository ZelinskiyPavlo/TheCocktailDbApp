package com.test.splash.di

import com.test.auth.di.AuthFeatureComponent
import com.test.dagger.scope.PerScreen
import com.test.splash.factory.SplashViewModelFactory
import dagger.Component
import dagger.Subcomponent
import dagger.android.AndroidInjectionModule

@PerScreen
@Component(dependencies = [AuthFeatureComponent::class],
    modules = [/*AndroidInjectionModule::class, */ContributeScreenModule::class])
interface SplashScreenComponent {

    fun splashViewModelFactory(): SplashViewModelFactory
}