package com.test.thecocktaildb.di.module

import com.test.thecocktaildb.ui.auth.AuthActivity
import com.test.thecocktaildb.ui.auth.login.LoginFragment
import com.test.thecocktaildb.ui.auth.register.RegisterFragment
import com.test.thecocktaildb.ui.auth.splash.SplashFragment
import com.test.thecocktaildb.ui.cocktailDetailsScreen.CocktailDetailsFragment
import com.test.thecocktaildb.ui.cocktailScreen.MainActivity
import com.test.thecocktaildb.ui.cocktailScreen.favoriteScreen.FavoriteFragment
import com.test.thecocktaildb.ui.cocktailScreen.filterScreen.FilterFragment
import com.test.thecocktaildb.ui.cocktailScreen.fragmentHostScreen.HostFragment
import com.test.thecocktaildb.ui.cocktailScreen.historyScreen.HistoryFragment
import com.test.thecocktaildb.ui.searchCocktailsScreen.SearchCocktailsFragment
import com.test.thecocktaildb.ui.setting.SettingFragment
import com.test.thecocktaildb.ui.setting.profile.ProfileFragment
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
    abstract fun contributeSearchCocktailsFragment(): SearchCocktailsFragment

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
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeCocktailsActivity(): MainActivity

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
}