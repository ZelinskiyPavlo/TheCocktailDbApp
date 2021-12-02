package com.test.thecocktaildb.di

import android.app.Application
import com.test.databaseimpl.di.DbModule
import com.test.firebase.di.FirebaseModule
import com.test.networkimpl.di.NetworkModule
import com.test.preference.di.LocalModule
import com.test.repositoryimpl.di.RepositoryModule
import com.test.thecocktaildb.CocktailApplication
import com.test.thecocktaildb.di.module.contribute.ActivityModule
import com.test.thecocktaildb.di.module.contribute.FragmentModule
import com.test.thecocktaildb.di.module.contribute.ServiceModule
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
        FirebaseModule::class, ServiceModule::class]
)
interface AppComponent : AndroidInjector<CocktailApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    override fun inject(instance: CocktailApplication)
}