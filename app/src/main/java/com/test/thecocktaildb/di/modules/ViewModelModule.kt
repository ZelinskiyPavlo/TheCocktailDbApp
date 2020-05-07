package com.test.thecocktaildb.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.thecocktaildb.cocktailDetailsScreen.CocktailDetailsViewModel
import com.test.thecocktaildb.cocktailsScreen.CocktailsViewModel
import com.test.thecocktaildb.di.ViewModelKey
import com.test.thecocktaildb.searchCocktailsScreen.SearchCocktailsViewModel
import com.test.thecocktaildb.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CocktailsViewModel::class)
    abstract fun bindsCocktailsViewModel(cocktailsViewModel: CocktailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CocktailDetailsViewModel::class)
    abstract fun bindsCocktailDetailsViewModel(
        cocktailDetailsViewModel: CocktailDetailsViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchCocktailsViewModel::class)
    abstract fun bindsSearchCocktailsViewModel(
        searchCocktailsViewModel: SearchCocktailsViewModel
    ): ViewModel

    @Binds
    abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}