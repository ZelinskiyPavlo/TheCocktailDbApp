package com.test.thecocktaildb.di

import android.app.Application
import com.test.thecocktaildb.CocktailsDbApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, FragmentModule::class])
interface AppComponent: AndroidInjector<CocktailsDbApplication> {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance application: CocktailsDbApplication): AppComponent
    }

    fun inject(app: Application)
}