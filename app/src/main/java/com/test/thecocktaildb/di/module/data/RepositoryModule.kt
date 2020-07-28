package com.test.thecocktaildb.di.module.data

import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.data.CocktailsRepository
import com.test.thecocktaildb.data.local.CocktailsLocalDataSourceImpl
import com.test.thecocktaildb.data.remote.CocktailsRemoteDataSourceImpl
import com.test.thecocktaildb.dataNew.db.impl.source.CocktailDbSourceImpl
import com.test.thecocktaildb.dataNew.db.source.UserDbSource
import com.test.thecocktaildb.dataNew.local.source.AppSettingLocalSource
import com.test.thecocktaildb.dataNew.local.source.TokenLocalSource
import com.test.thecocktaildb.dataNew.network.source.AuthNetSource
import com.test.thecocktaildb.dataNew.network.source.UserNetSource
import com.test.thecocktaildb.dataNew.repository.impl.mapper.CocktailRepoModelMapper
import com.test.thecocktaildb.dataNew.repository.impl.mapper.UserRepoModelMapper
import com.test.thecocktaildb.dataNew.repository.impl.source.*
import com.test.thecocktaildb.dataNew.repository.source.*
import com.test.thecocktaildb.util.scheduler.AppSchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideCocktailRepository(
        cocktailDbSourceImpl: CocktailDbSourceImpl,
        mapper: CocktailRepoModelMapper
    ): CocktailRepository = CocktailRepositoryImpl(cocktailDbSourceImpl, mapper)

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
        userModelMapper: UserRepoModelMapper,
    ): UserRepository = UserRepositoryImpl(userDbSource, userNetSource, userModelMapper)

    @Singleton
    @Provides
    fun provideAppSettingRepository(
        appSettingLocalSource: AppSettingLocalSource
    ): AppSettingRepository = AppSettingRepositoryImpl(appSettingLocalSource)

    // TODO: 27.07.2020 old repository provider
    @Singleton
    @Provides
    fun provideOldRepository(
        cocktailsRemoteDataSourceImpl: CocktailsRemoteDataSourceImpl,
        cocktailsLocalDataSourceImpl: CocktailsLocalDataSourceImpl,
        scheduler: AppSchedulerProvider
    ): CocktailsRepository =
        AppCocktailsRepository(
            cocktailsRemoteDataSourceImpl, cocktailsLocalDataSourceImpl, scheduler
        )
}