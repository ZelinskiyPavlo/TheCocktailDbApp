package com.test.preference.di

import android.app.Application
import android.content.Context
import com.test.local.LocalConstant
import com.test.local.source.AppSettingLocalSource
import com.test.local.source.TokenLocalSource
import com.test.preference.impl.SharedPrefsHelper
import com.test.preference.impl.source.AppSettingLocalSourceImpl
import com.test.preference.impl.source.TokenLocalSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule {

    @Singleton
    @Provides
    fun provideAppSettingLocalSource(sharedPrefsHelper: SharedPrefsHelper): AppSettingLocalSource =
        AppSettingLocalSourceImpl(sharedPrefsHelper)

    @Singleton
    @Provides
    fun provideTokenLocalSource(sharedPrefsHelper: SharedPrefsHelper): TokenLocalSource =
        TokenLocalSourceImpl(sharedPrefsHelper)

    @Singleton
    @Provides
    fun provideSharedPrefsHelper(app: Application): SharedPrefsHelper =
        SharedPrefsHelper(
            app.applicationContext.getSharedPreferences(
                LocalConstant.SHARED_PREFS,
                Context.MODE_PRIVATE
            )
        )

}