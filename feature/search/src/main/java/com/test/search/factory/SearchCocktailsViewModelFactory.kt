package com.test.search.factory

import androidx.lifecycle.SavedStateHandle
import com.test.presentation.factory.ViewModelAssistedFactory
import com.test.presentation.mapper.cocktail.CocktailModelMapper
import com.test.repository.source.CocktailRepository
import com.test.search.ui.SearchCocktailViewModel
import javax.inject.Inject

internal class SearchCocktailsViewModelFactory @Inject constructor(
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper,
) : ViewModelAssistedFactory<SearchCocktailViewModel> {
    override fun create(handle: SavedStateHandle): SearchCocktailViewModel {
        return SearchCocktailViewModel(handle, cocktailRepo, cocktailMapper)
    }
}
