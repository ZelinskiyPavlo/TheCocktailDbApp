package com.test.thecocktaildb.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.thecocktaildb.di.ViewModelKey
import com.test.thecocktaildb.ui.cocktail.favorite.FavoriteViewModel
import com.test.thecocktaildb.ui.cocktail.filter.CocktailFilterViewModel
import com.test.thecocktaildb.ui.cocktail.history.CocktailsViewModel
import com.test.thecocktaildb.ui.cocktail.host.HostViewModel
import com.test.thecocktaildb.ui.detail.CocktailDetailsViewModel
import com.test.thecocktaildb.ui.profile.ProfileViewModel
import com.test.thecocktaildb.ui.search.SearchCocktailsViewModel
import com.test.thecocktaildb.util.vmfactory.ViewModelFactory
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