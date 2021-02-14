package com.test.thecocktaildb.di

import android.app.Application
import com.test.firebase.di.FirebaseModule
import com.test.impl.di.DbModule
import com.test.impl.di.NetworkModule
import com.test.impl.di.RepositoryModule
import com.test.preference.di.LocalModule
import com.test.thecocktaildb.CocktailApplication
import com.test.thecocktaildb.di.module.ActivityModule
import com.test.thecocktaildb.di.module.FragmentModule
import com.test.thecocktaildb.di.module.navigation.global.GlobalCiceroneModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        GlobalCiceroneModule::class, FragmentModule::class, ActivityModule::class,
        DbModule::class, LocalModule::class, NetworkModule::class, RepositoryModule::class,
        FirebaseModule::class]
)
interface AppComponent : AndroidInjector<CocktailApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    override fun inject(instance: CocktailApplication)
}