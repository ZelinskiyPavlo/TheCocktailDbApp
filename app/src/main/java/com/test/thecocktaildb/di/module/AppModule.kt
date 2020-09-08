package com.test.thecocktaildb.di.module

import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.data.CocktailsRepository
import com.test.thecocktaildb.data.local.CocktailsLocalDataSourceImpl
import com.test.thecocktaildb.data.remote.CocktailsRemoteDataSourceImpl
import com.test.thecocktaildb.util.scheduler.AppSchedulerProvider
import com.test.thecocktaildb.util.scheduler.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DatabaseModule::class, RetrofitModule::class, NewDataModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideRepository(
        cocktailsRemoteDataSourceImpl: CocktailsRemoteDataSourceImpl,
        cocktailsLocalDataSourceImpl: CocktailsLocalDataSourceImpl,
        scheduler: AppSchedulerProvider
    ): CocktailsRepository =
        AppCocktailsRepository(cocktailsRemoteDataSourceImpl, cocktailsLocalDataSourceImpl, scheduler)


    @Singleton
    @Provides
    fun provideScheduler(): SchedulerProvider = AppSchedulerProvider()
}