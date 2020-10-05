package com.test.thecocktaildb.di.module

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.test.thecocktaildb.CocktailApplication
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @Provides
    fun provideFirebaseAnalytics(app: CocktailApplication): FirebaseAnalytics = FirebaseAnalytics.getInstance(app)

    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
}