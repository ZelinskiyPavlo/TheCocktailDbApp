package com.test.thecocktaildb.ui.detail

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.data.CocktailsRepository
import com.test.thecocktaildb.ui.base.BaseViewModel
import com.test.thecocktaildb.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class CocktailDetailsViewModel (
    savedStateHandle: SavedStateHandle,
    private val repository: CocktailsRepository) :
    BaseViewModel(savedStateHandle) {

    private val _cocktailPictureLiveData = MutableLiveData<String>()
    val cocktailPictureLiveData: LiveData<String> = _cocktailPictureLiveData

    private val _cocktailNameLiveData = MutableLiveData<String>()
    val cocktailNameLiveData: LiveData<String> = _cocktailNameLiveData

    private val _cocktailAlcoholicLiveData = MutableLiveData<String>()
    val cocktailAlcoholicLiveData: LiveData<String> = _cocktailAlcoholicLiveData

    private val _cocktailGlassLiveData = MutableLiveData<String>()
    val cocktailGlassLiveData: LiveData<String> = _cocktailGlassLiveData

    private val _ingredientsListLiveData = MutableLiveData<List<Ingredient>>()
    val ingredientsLiveData: LiveData<List<Ingredient>> = _ingredientsListLiveData

    private val _cocktailInstructionLiveData = MutableLiveData<String>()
    val cocktailInstructionLiveData: LiveData<String> = _cocktailInstructionLiveData

    private val _onBackPressedEventLiveData = MutableLiveData<Event<Unit>>()
    val onBackPressedEventLiveData: LiveData<Event<Unit>> = _onBackPressedEventLiveData

    lateinit var cocktailId: String

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    fun getCocktailById(cocktailId: String) {
        disposable.add(
            repository.getCocktail(cocktailId).subscribeBy(onSuccess = { fillCocktailDetails(it) },
                onError = { Timber.e("Error occurred in getting cocktails, $it") })
        )
    }

    private fun fillCocktailDetails(cocktail: Cocktail) {
        _cocktailPictureLiveData.value = cocktail.strDrinkThumb
        _cocktailNameLiveData.value = cocktail.strDrink
        _cocktailAlcoholicLiveData.value = cocktail.strAlcoholic
        _cocktailGlassLiveData.value = cocktail.strGlass
        _ingredientsListLiveData.value = cocktail.createNumberedIngredientsList()
        _cocktailInstructionLiveData.value = cocktail.strInstructions

        cocktailId = cocktail.idDrink
    }

    fun onBackButtonPressed(view: View?) {
        _onBackPressedEventLiveData.value = Event(Unit)
    }
}