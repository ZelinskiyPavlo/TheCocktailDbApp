package com.test.thecocktaildb.di.module.contribute

import com.test.dagger.scope.PerActivity
import com.test.thecocktaildb.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule {

    @Suppress("unused")
    @PerActivity
    @ContributesAndroidInjector
    fun contributeMainActivity(): MainActivity

}