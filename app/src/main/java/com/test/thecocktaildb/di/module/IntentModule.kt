package com.test.thecocktaildb.di.module

import android.app.Application
import android.content.Intent
import com.test.dagger.DiConstant
import com.test.thecocktaildb.ui.MainActivity
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class IntentModule {
    /** I can't really think about different way to specify activity class for intent from different
     *  than app module.
    * */

    @Provides
    @Named(DiConstant.ACTIVITY_INTENT)
    fun provideActivityIntent(application: Application) =
        Intent(application.applicationContext, MainActivity::class.java)
}