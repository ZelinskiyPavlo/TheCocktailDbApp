package com.test.auth.di

import android.app.Application
import com.test.auth.ui.AuthActivity
import com.test.dagger.AppDependencies
import com.test.dagger.CoreComponent
import com.test.dagger.scope.PerFeature
import com.test.navigation.auth.LoginNavigator
import com.test.navigation.auth.RegisterNavigator
import com.test.navigation.auth.SplashNavigator
import com.test.repository.source.UserRepository
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import me.vponomarenko.injectionmanager.x.XInjectionManager

@PerFeature
@Component(
    dependencies = [AppDependencies::class],
    modules = [AndroidInjectionModule::class, ContributeScreenModule::class]
)
interface AuthFeatureComponent/* : AndroidInjector<Application>*/ {

    class Initializer private constructor() {
        companion object {
            fun init(): AuthFeatureComponent =
                DaggerAuthFeatureComponent.builder()
                    .appDependencies(XInjectionManager.findComponent())
                    .build()
        }
    }

    fun inject(authActivity: AuthActivity)

    fun userRepository(): UserRepository

    fun splashNavigator(): SplashNavigator
    fun loginNavigator(): LoginNavigator
    fun registerNavigator(): RegisterNavigator
}