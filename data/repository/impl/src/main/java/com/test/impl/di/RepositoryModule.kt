package com.test.impl.di

import com.test.impl.source.*
import com.test.repository.source.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Suppress("unused")
    @Singleton
    @Binds
    fun provideCocktailRepository(
        cocktailRepositoryImpl: CocktailRepositoryImpl
    ): CocktailRepository

    @Suppress("unused")
    @Singleton
    @Binds
    fun provideAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Suppress("unused")
    @Singleton
    @Binds
    fun provideTokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl
    ): TokenRepository

    @Suppress("unused")
    @Singleton
    @Binds
    fun provideUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Suppress("unused")
    @Singleton
    @Binds
    fun provideAppSettingRepository(
        appSettingRepositoryImpl: AppSettingRepositoryImpl
    ): AppSettingRepository

    @Suppress("unused")
    @Singleton
    @Binds
    fun provideEventRepository(
        eventRepositoryImpl: EventRepositoryImpl
    ): EventRepository
}