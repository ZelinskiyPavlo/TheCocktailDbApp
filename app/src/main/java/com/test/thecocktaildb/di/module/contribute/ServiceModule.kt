package com.test.thecocktaildb.di.module.contribute

import com.test.dagger.scope.PerService
import com.test.fcm.FcmService
import com.test.thecocktaildb.di.module.IntentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ServiceModule {

    @Suppress("unused")
    @PerService
    @ContributesAndroidInjector(modules = [IntentModule::class])
    fun contributeFcmService(): FcmService
}