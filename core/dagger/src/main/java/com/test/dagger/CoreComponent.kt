package com.test.dagger

import android.app.Application
import com.test.di.DbModule
import com.test.di.NetworkModule
import com.test.firebase.FirebaseModule
import com.test.impl.di.NavigationModule
import com.test.impl.di.RepositoryModule
import com.test.navigation.auth.LoginNavigator
import com.test.navigation.auth.RegisterNavigator
import com.test.navigation.auth.SplashNavigator
import com.test.preference.di.LocalModule
import com.test.repository.source.UserRepository
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DbModule::class, NetworkModule::class, RepositoryModule::class, LocalModule::class,
    FirebaseModule::class, AndroidInjectionModule::class, NavigationModule::class]
)
interface CoreComponent : AndroidInjector<Application> {

    @Component.Builder
    interface Builder {
        fun build(): CoreComponent

        @BindsInstance
        fun application(application: Application): Builder
    }

    fun userRepository(): UserRepository

    fun splashNavigator(): SplashNavigator
    fun loginNavigator(): LoginNavigator
    fun registerNavigator(): RegisterNavigator
}