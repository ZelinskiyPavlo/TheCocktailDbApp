package com.test.detail.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.test.detail.api.DetailCommunicationApi
import com.test.detail.model.Ingredient
import com.test.presentation.mapper.cocktail.CocktailModelMapper
import com.test.presentation.model.cocktail.CocktailModel
import com.test.presentation.model.cocktail.type.CocktailIngredient
import com.test.presentation.ui.base.BaseViewModel
import com.test.presentation.util.WhileViewSubscribed
import com.test.repository.source.CocktailRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CocktailDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper,
    private val communicationApi: DetailCommunicationApi
) : BaseViewModel(savedStateHandle) {

    private val cocktailFlow = MutableSharedFlow<CocktailModel?>(replay = 1)

    val cocktailPictureFlow = cocktailFlow.filterNotNull().map { it.image }
        .stateIn(viewModelScope, WhileViewSubscribed, null)

    val cocktailNameFlow = cocktailFlow.filterNotNull().map { it.names.defaults }
        .stateIn(viewModelScope, WhileViewSubscribed, "")

    val cocktailAlcoholicFlow = cocktailFlow.filterNotNull().map { it.alcoholType.key }
        .stateIn(viewModelScope, WhileViewSubscribed, "")

    val cocktailGlassFlow = cocktailFlow.filterNotNull().map { it.glass.key }
        .stateIn(viewModelScope, WhileViewSubscribed, "")

    internal val ingredientsFlow =
        cocktailFlow.filterNotNull().map {
            it.ingredients.zip(it.measures) { ingredient: CocktailIngredient, measure: String ->
                Ingredient(ingredient.key, measure)
            }
        }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    val cocktailInstructionFlow = cocktailFlow.filterNotNull().map { it.instructions.defaults }
        .stateIn(viewModelScope, WhileViewSubscribed, "")

    val isCocktailFoundFlow = cocktailFlow.map { it != null }
        .stateIn(viewModelScope, WhileViewSubscribed, null)

    var cocktailId = cocktailFlow.filterNotNull().map { it.id }
        .stateIn(viewModelScope, WhileViewSubscribed, -1L)

    fun cocktailNotFound() {
        communicationApi.sendNoCocktailWithIdFoundEvent()
    }

    fun getCocktailById(cocktailId: Long) {
        launchRequest {
            cocktailFlow.emit(cocktailRepo.getCocktailById(cocktailId)?.run(cocktailMapper::mapTo))
        }
    }
}