package com.test.thecocktaildb.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.thecocktaildb.di.ViewModelKey
import com.test.thecocktaildb.ui.cocktailDetailsScreen.CocktailDetailsViewModel
import com.test.thecocktaildb.ui.cocktailScreen.favoriteScreen.FavoriteViewModel
import com.test.thecocktaildb.ui.cocktailScreen.filterScreen.FilterViewModel
import com.test.thecocktaildb.ui.cocktailScreen.fragmentHostScreen.HostViewModel
import com.test.thecocktaildb.ui.cocktailScreen.historyScreen.HistoryViewModel
import com.test.thecocktaildb.ui.profileScreen.ProfileViewModel
import com.test.thecocktaildb.ui.searchCocktailsScreen.SearchCocktailsViewModel
import com.test.thecocktaildb.util.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    @Suppress("unused")
    abstract fun bindsCocktailsViewModel(historyViewModel: HistoryViewModel): ViewModel

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
    @ViewModelKey(FilterViewModel::class)
    @Suppress("unused")
    abstract fun bindsCocktailFilterViewModel(
        filterViewModel: FilterViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HostViewModel::class)
    @Suppress("unused")
    abstract fun bindsHostViewModel(
        hostViewModel: HostViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    @Suppress("unused")
    abstract fun bindsFavoriteViewModel(
        hostViewModel: FavoriteViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    @Suppress("unused")
    abstract fun bindsProfileViewModel(
        profileViewModel: ProfileViewModel
    ): ViewModel

    @Binds
    @Suppress("unused")
    abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}