package com.test.thecocktaildb.di

import android.app.Application
import com.test.thecocktaildb.CocktailApplication
import com.test.thecocktaildb.di.module.AppModule
import com.test.thecocktaildb.di.module.FirebaseModule
import com.test.thecocktaildb.di.module.FragmentModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class, AppModule::class, FragmentModule::class,
        FirebaseModule::class]
)
interface AppComponent : AndroidInjector<CocktailApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: CocktailApplication): AppComponent
    }

    fun inject(app: Application)
}