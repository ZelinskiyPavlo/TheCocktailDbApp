package com.test.thecocktaildb.di.module.navigation.feature

import com.test.profile.api.ProfileNavigationApi
import com.test.thecocktaildb.feature.profile.ProfileNavigationImpl
import dagger.Binds
import dagger.Module

@Module
interface ProfileNavigationModule {

    @Suppress("unused")
    @Binds
    fun bindProfileNavigation(profileNavigationImpl: ProfileNavigationImpl): ProfileNavigationApi
}