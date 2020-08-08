package com.test.thecocktaildb.di.module.data

import com.test.thecocktaildb.dataNew.db.impl.source.CocktailDbSourceImpl
import com.test.thecocktaildb.dataNew.db.source.UserDbSource
import com.test.thecocktaildb.dataNew.local.source.AppSettingLocalSource
import com.test.thecocktaildb.dataNew.local.source.TokenLocalSource
import com.test.thecocktaildb.dataNew.network.impl.source.CocktailNetSourceImpl
import com.test.thecocktaildb.dataNew.network.source.AuthNetSource
import com.test.thecocktaildb.dataNew.network.source.UserNetSource
import com.test.thecocktaildb.dataNew.network.source.UserUploadNetSource
import com.test.thecocktaildb.dataNew.repository.impl.mapper.CocktailRepoModelMapper
import com.test.thecocktaildb.dataNew.repository.impl.mapper.UserRepoModelMapper
import com.test.thecocktaildb.dataNew.repository.impl.source.*
import com.test.thecocktaildb.dataNew.repository.source.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideCocktailRepository(
        cocktailDbSourceImpl: CocktailDbSourceImpl,
        cocktailNetSourceImpl: CocktailNetSourceImpl,
        mapper: CocktailRepoModelMapper
    ): CocktailRepository =
        CocktailRepositoryImpl(cocktailDbSourceImpl, cocktailNetSourceImpl, mapper)

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