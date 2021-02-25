package com.test.detail.factory

import androidx.lifecycle.SavedStateHandle
import com.test.detail.api.DetailCommunicationApi
import com.test.detail.ui.CocktailDetailsViewModel
import com.test.presentation.factory.ViewModelAssistedFactory
import com.test.presentation.mapper.cocktail.CocktailModelMapper
import com.test.repository.source.CocktailRepository
import javax.inject.Inject

internal class CocktailDetailsViewModelFactory @Inject constructor(
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper,
    private val communicationApi: DetailCommunicationApi
) : ViewModelAssistedFactory<CocktailDetailsViewModel> {
    override fun create(handle: SavedStateHandle): CocktailDetailsViewModel {
        return CocktailDetailsViewModel(handle, cocktailRepo, cocktailMapper, communicationApi)
    }
}
