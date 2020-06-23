package com.test.thecocktaildb.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.thecocktaildb.di.ViewModelKey
import com.test.thecocktaildb.ui.cocktailDetailsScreen.CocktailDetailsViewModel
import com.test.thecocktaildb.ui.cocktailsScreen.CocktailFilterViewModel
import com.test.thecocktaildb.ui.cocktailsScreen.CocktailsViewModel
import com.test.thecocktaildb.ui.searchCocktailsScreen.SearchCocktailsViewModel
import com.test.thecocktaildb.util.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CocktailsViewModel::class)
    @Suppress("unused")
    abstract fun bindsCocktailsViewModel(cocktailsViewModel: CocktailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CocktailDetailsViewModel::class)
    @Suppress("unused")
    abstract fun bindsCocktailDetailsViewModel(
        cocktailDetailsViewModel: CocktailDetailsViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchCocktailsViewModel::class)
    @Suppress("unused")
    abstract fun bindsSearchCocktailsViewModel(
        searchCocktailsViewModel: SearchCocktailsViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CocktailFilterViewModel::class)
    @Suppress("unused")
    abstract fun bindsCocktailFilterViewModel(
        cocktailFilterViewModel: CocktailFilterViewModel
    ): ViewModel

    @Binds
    @Suppress("unused")
    abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}