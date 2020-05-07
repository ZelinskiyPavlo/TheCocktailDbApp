package com.test.thecocktaildb.di

import android.app.Application
import com.test.thecocktaildb.CocktailsApplication
import com.test.thecocktaildb.di.modules.AppModule
import com.test.thecocktaildb.di.modules.FragmentModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, FragmentModule::class])
interface AppComponent: AndroidInjector<CocktailsApplication> {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance application: CocktailsApplication): AppComponent
    }

    fun inject(app: Application)
}