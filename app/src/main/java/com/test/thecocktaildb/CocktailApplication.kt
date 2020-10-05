package com.test.thecocktaildb

import android.app.Application
import com.test.dagger.DaggerCoreComponent
import com.test.thecocktaildb.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class CocktailApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

//        DaggerAppComponent.factory().create(this).inject(this)
//        AppInjector.init(this)

        DaggerCoreComponent.builder().application(this).build().inject(this)
    }
}