package com.test.thecocktaildb

import android.app.Application
import com.test.dagger.AppDependencies
import com.test.di.DbModule
import com.test.di.NetworkModule
import com.test.firebase.FirebaseModule
import com.test.impl.di.NavigationModule
import com.test.impl.di.RepositoryModule
import com.test.preference.di.LocalModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DbModule::class, NetworkModule::class, RepositoryModule::class, LocalModule::class,
        FirebaseModule::class, AndroidInjectionModule::class, NavigationModule::class]
)
interface CoreComponent : AppDependencies {

    @Component.Builder
    interface Builder {
        fun build(): CoreComponent

        @BindsInstance
        fun application(application: Application): Builder
    }

//    class Initializer private constructor() {
//        companion object {
//            fun init(): AppComponent = DaggerCoreComponent.create()
//        }
//    }

//    fun userRepository(): UserRepository

//    fun splashNavigator(): SplashNavigator
//    fun loginNavigator(): LoginNavigator
//    fun registerNavigator(): RegisterNavigator
}