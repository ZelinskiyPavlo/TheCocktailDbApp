package com.test.detail.ui

import androidx.lifecycle.*
import com.test.detail.api.DetailCommunicationApi
import com.test.detail.model.Ingredient
import com.test.presentation.extension.mapNotNull
import com.test.presentation.mapper.cocktail.CocktailModelMapper
import com.test.presentation.model.cocktail.CocktailModel
import com.test.presentation.model.cocktail.type.CocktailIngredient
import com.test.presentation.ui.base.BaseViewModel
import com.test.repository.source.CocktailRepository

class CocktailDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper,
    private val communicationApi: DetailCommunicationApi
) :
    BaseViewModel(savedStateHandle) {

    private val cocktailLiveData = MutableLiveData<CocktailModel?>()

    val cocktailPictureLiveData: LiveData<String> =
        cocktailLiveData.mapNotNull { image }

    val cocktailNameLiveData: LiveData<String> =
        cocktailLiveData.mapNotNull { names.defaults!! }

    val cocktailAlcoholicLiveData: LiveData<String> =
        cocktailLiveData.mapNotNull { alcoholType.key }

    val cocktailGlassLiveData: LiveData<String> =
        cocktailLiveData.mapNotNull { glass.key }

    internal val ingredientsLiveData: LiveData<List<Ingredient>> =
        cocktailLiveData.mapNotNull {
            ingredients.zip(measures) { ingredient: CocktailIngredient, measure: String ->
                Ingredient(ingredient.key, measure)
            }
        }

    val cocktailInstructionLiveData: LiveData<String> =
        cocktailLiveData.mapNotNull { instructions.defaults!! }

    val isCocktailFoundLiveData: LiveData<Boolean> =
        cocktailLiveData.map { it != null }

    var cocktailId: Long = -1L

    private val isCocktailFoundObserver = Observer<Boolean> { result ->
        if (result == false) communicationApi.sendNoCocktailWithIdFoundEvent()
    }

    init {
        isCocktailFoundLiveData.observeForever(isCocktailFoundObserver)
    }

    override fun onCleared() {
        isCocktailFoundLiveData.removeObserver(isCocktailFoundObserver)
        super.onCleared()
    }

    fun getCocktailById(cocktailId: Long) {
        this.cocktailId = cocktailId
        launchRequest(cocktailLiveData) {
            cocktailRepo.getCocktailById(cocktailId)?.run(cocktailMapper::mapTo)
        }
    }
}