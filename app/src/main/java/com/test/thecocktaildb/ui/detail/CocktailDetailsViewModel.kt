package com.test.thecocktaildb.ui.detail

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import com.test.thecocktaildb.dataNew.repository.source.CocktailRepository
import com.test.thecocktaildb.presentationNew.mapper.CocktailModelMapper
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailIngredient
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailModel
import com.test.thecocktaildb.ui.base.BaseViewModel
import com.test.thecocktaildb.util.Event
import io.reactivex.disposables.CompositeDisposable

class CocktailDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper,
) :
    BaseViewModel(savedStateHandle) {

    private val cocktailLiveData = MutableLiveData<CocktailModel>()

    val cocktailPictureLiveData: LiveData<String> =
        Transformations.map(cocktailLiveData) { it.image }

    val cocktailNameLiveData: LiveData<String> =
        Transformations.map(cocktailLiveData) { it.names.defaults }

    val cocktailAlcoholicLiveData: LiveData<String> =
        Transformations.map(cocktailLiveData) { it.alcoholType.key }

    val cocktailGlassLiveData: LiveData<String> =
        Transformations.map(cocktailLiveData) { it.glass.key }

    val ingredientsLiveData: LiveData<List<Ingredient>> =
        Transformations.map(cocktailLiveData) {
            it.ingredients.zip(it.measures) { ingredient: CocktailIngredient, measure: String ->
                Ingredient(ingredient.key, measure)
            }
        }

    val cocktailInstructionLiveData: LiveData<String> =
        Transformations.map(cocktailLiveData) { it.instructions.defaults }

    var cocktailId: Long = -1L

    private val _onBackPressedEventLiveData = MutableLiveData<Event<Unit>>()
    val onBackPressedEventLiveData: LiveData<Event<Unit>> = _onBackPressedEventLiveData

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    fun getCocktailById(cocktailId: Long) {
        this.cocktailId = cocktailId
        launchRequest(cocktailLiveData) {
            cocktailRepo.getCocktailById(cocktailId)?.run(cocktailMapper::mapTo)
        }
    }

    fun onBackButtonPressed(view: View?) {
        _onBackPressedEventLiveData.value = Event(Unit)
    }
}