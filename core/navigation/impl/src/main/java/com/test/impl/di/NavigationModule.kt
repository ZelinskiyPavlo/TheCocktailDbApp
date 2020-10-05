package com.test.impl.di

import com.test.impl.auth.LoginNavigatorImpl
import com.test.impl.auth.RegisterNavigatorImpl
import com.test.impl.auth.SplashNavigatorImpl
import com.test.navigation.auth.LoginNavigator
import com.test.navigation.auth.RegisterNavigator
import com.test.navigation.auth.SplashNavigator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NavigationModule {

    @Provides
    @Singleton
    fun provideSplashNavigator(): SplashNavigator = SplashNavigatorImpl()

    @Provides
    @Singleton
    fun provideLoginNavigator(): LoginNavigator = LoginNavigatorImpl()

    @Provides
    @Singleton
    fun provideRegisterNavigator(): RegisterNavigator = RegisterNavigatorImpl()
}