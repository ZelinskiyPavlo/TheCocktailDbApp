package com.test.firebase

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @Provides
    fun provideFirebaseAnalytics(app: Application) = FirebaseAnalytics.getInstance(app)

    @Provides
    fun provideFirebaseRemoteConfig() = FirebaseRemoteConfig.getInstance()
}