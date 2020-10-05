package com.test.impl.di

import com.test.database.source.CocktailDbSource
import com.test.database.source.UserDbSource
import com.test.local.source.AppSettingLocalSource
import com.test.local.source.TokenLocalSource
import com.test.network.source.AuthNetSource
import com.test.network.source.UserNetSource
import com.test.network.source.UserUploadNetSource
import com.test.repository.source.*
import com.test.impl.mapper.CocktailRepoModelMapper
import com.test.impl.mapper.UserRepoModelMapper
import com.test.impl.source.*
import com.test.network.source.CocktailNetSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideCocktailRepository(
        cocktailDbSource: CocktailDbSource,
        cocktailNetSource: CocktailNetSource,
        mapper: CocktailRepoModelMapper
    ): CocktailRepository =
        CocktailRepositoryImpl(cocktailDbSource, cocktailNetSource, mapper)

    @Singleton
    @Provides
    fun provideAuthRepository(
        authNetSource: AuthNetSource,
        userNetSource: UserNetSource,
        userDbSource: UserDbSource,
        userModelMapper: UserRepoModelMapper,
        tokenLocalSource: TokenLocalSource
    ): AuthRepository =
        AuthRepositoryImpl(
            authNetSource, userNetSource, userDbSource, userModelMapper, tokenLocalSource
        )

    @Singleton
    @Provides
    fun provideTokenRepository(tokenLocalSource: TokenLocalSource):
            TokenRepository = TokenRepositoryImpl(tokenLocalSource)

    @Singleton
    @Provides
    fun provideUserRepository(
        userDbSource: UserDbSource,
        userNetSource: UserNetSource,
        userUploadNetSource: UserUploadNetSource,
        userModelMapper: UserRepoModelMapper,
    ): UserRepository =
        UserRepositoryImpl(userDbSource, userNetSource, userUploadNetSource, userModelMapper)

    @Singleton
    @Provides
    fun provideAppSettingRepository(
        appSettingLocalSource: AppSettingLocalSource
    ): AppSettingRepository = AppSettingRepositoryImpl(appSettingLocalSource)
}