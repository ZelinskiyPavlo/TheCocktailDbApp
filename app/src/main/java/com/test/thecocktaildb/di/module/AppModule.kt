package com.test.thecocktaildb.di.module

import com.test.thecocktaildb.di.module.data.DbModule
import com.test.thecocktaildb.di.module.data.LocalModule
import com.test.thecocktaildb.di.module.data.NetworkModule
import com.test.thecocktaildb.di.module.data.RepositoryModule
import dagger.Module

@Module(
    includes = [DbModule::class, NetworkModule::class, RepositoryModule::class, LocalModule::class]
)
class AppModule