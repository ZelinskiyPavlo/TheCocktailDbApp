package com.test.thecocktaildb.di.module

import com.test.thecocktaildb.presentation.ui.auth.AuthActivity
import com.test.thecocktaildb.presentation.ui.auth.login.LoginFragment
import com.test.thecocktaildb.presentation.ui.auth.register.RegisterFragment
import com.test.thecocktaildb.presentation.ui.auth.splash.SplashFragment
import com.test.thecocktaildb.presentation.ui.cocktail.MainActivity
import com.test.thecocktaildb.presentation.ui.cocktail.favorite.FavoriteFragment
import com.test.thecocktaildb.presentation.ui.cocktail.filter.FilterFragment
import com.test.thecocktaildb.presentation.ui.cocktail.history.HistoryFragment
import com.test.thecocktaildb.presentation.ui.cocktail.host.HostFragment
import com.test.thecocktaildb.presentation.ui.detail.CocktailDetailsFragment
import com.test.thecocktaildb.presentation.ui.search.SearchCocktailFragment
import com.test.thecocktaildb.presentation.ui.setting.SettingFragment
import com.test.thecocktaildb.presentation.ui.setting.cube.CubeFragment
import com.test.thecocktaildb.presentation.ui.setting.profile.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeCocktailsFragment(): HistoryFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeCocktailDetailsFragment(): CocktailDetailsFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeSearchCocktailsFragment(): SearchCocktailFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeFilterFragment(): FilterFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeHostFragment(): HostFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeFavoriteFragment(): FavoriteFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeSettingFragment(): SettingFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeAuthActivity(): AuthActivity

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeSplashFragment(): SplashFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeCubeFragment(): CubeFragment
}