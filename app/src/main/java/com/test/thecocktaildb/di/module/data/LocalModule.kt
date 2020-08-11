package com.test.thecocktaildb.di.module.data

import android.content.Context
import com.test.thecocktaildb.CocktailApplication
import com.test.thecocktaildb.data.local.LocalConstant
import com.test.thecocktaildb.data.local.impl.SharedPrefsHelper
import com.test.thecocktaildb.data.local.impl.source.AppSettingLocalSourceImpl
import com.test.thecocktaildb.data.local.impl.source.TokenLocalSourceImpl
import com.test.thecocktaildb.data.local.source.AppSettingLocalSource
import com.test.thecocktaildb.data.local.source.TokenLocalSource
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
    fun provideSharedPrefsHelper(app: CocktailApplication): SharedPrefsHelper =
        SharedPrefsHelper(
            app.applicationContext.getSharedPreferences(
                LocalConstant.SHARED_PREFS,
                Context.MODE_PRIVATE
            )
        )

}