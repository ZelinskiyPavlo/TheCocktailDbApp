package com.test.thecocktaildb.di.module

import com.test.thecocktaildb.di.module.data.DbModule
import com.test.thecocktaildb.di.module.data.LocalModule
import com.test.thecocktaildb.di.module.data.NetworkModule
import com.test.thecocktaildb.di.module.data.RepositoryModule
import com.test.thecocktaildb.util.scheduler.AppSchedulerProvider
import com.test.thecocktaildb.util.scheduler.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [DatabaseModule::class, RetrofitModule::class, DbModule::class,
        NetworkModule::class, RepositoryModule::class, LocalModule::class]
)
class AppModule {

    @Singleton
    @Provides
    fun provideScheduler(): SchedulerProvider = AppSchedulerProvider()
}