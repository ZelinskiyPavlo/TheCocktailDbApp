package com.test.repositoryimpl.di

import com.test.repository.source.AppSettingRepository
import com.test.repository.source.AuthRepository
import com.test.repository.source.CocktailRepository
import com.test.repository.source.EventRepository
import com.test.repository.source.TokenRepository
import com.test.repository.source.UserRepository
import com.test.repositoryimpl.source.AppSettingRepositoryImpl
import com.test.repositoryimpl.source.AuthRepositoryImpl
import com.test.repositoryimpl.source.CocktailRepositoryImpl
import com.test.repositoryimpl.source.EventRepositoryImpl
import com.test.repositoryimpl.source.TokenRepositoryImpl
import com.test.repositoryimpl.source.UserRepositoryImpl
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