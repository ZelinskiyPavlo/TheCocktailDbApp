package com.test.cocktail.factory

import androidx.lifecycle.SavedStateHandle
import com.test.cocktail.ui.CocktailViewModel
import com.test.presentation.factory.ViewModelAssistedFactory
import com.test.presentation.mapper.cocktail.CocktailModelMapper
import com.test.presentation.mapper.user.UserModelMapper
import com.test.repository.source.CocktailRepository
import com.test.repository.source.UserRepository
import javax.inject.Inject

internal class CocktailViewModelFactory @Inject constructor(
    private val cocktailRepo: CocktailRepository,
    private val userRepo: UserRepository,
    private val cocktailMapper: CocktailModelMapper,
    private val userMapper: UserModelMapper
) : ViewModelAssistedFactory<CocktailViewModel> {
    override fun create(handle: SavedStateHandle): CocktailViewModel {
        return CocktailViewModel(handle, cocktailRepo, userRepo, cocktailMapper, userMapper)
    }
}
