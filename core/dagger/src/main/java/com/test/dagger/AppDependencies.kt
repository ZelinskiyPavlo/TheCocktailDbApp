package com.test.dagger

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.test.navigation.auth.LoginNavigator
import com.test.navigation.auth.RegisterNavigator
import com.test.navigation.auth.SplashNavigator
import com.test.repository.source.UserRepository

// TODO: Якщо я правильно розумію, то я тут маю передати тільки інтерфейси які мені будуть потрібні?
//      Чи потрібно передати абсолютно все, я думаю потрібно передати абсолютно все. Спочатку ми спробуємо сюди передати
//      абсолюно всі модулі включно з тими які використовуються в інших. Хотя ні, тоді нам треба буде роздати дуже багато прав.
//      Попробуємо передати сюди тільки Інтерфейси. Можна навіть по мінімуму

interface AppDependencies {

    fun userRepository(): UserRepository

    fun splashNavigator(): SplashNavigator
    fun loginNavigator(): LoginNavigator
    fun registerNavigator(): RegisterNavigator

    fun provideFirebaseAnalytics(): FirebaseAnalytics
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig
}