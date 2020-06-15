package com.test.thecocktaildb.ui.cocktailDetailsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.data.Cocktail
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class CocktailDetailsViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel() {

    private val _cocktailPicture = MutableLiveData<String>()
    val cocktailPicture: LiveData<String> = _cocktailPicture

    private val _cocktailName = MutableLiveData<String>()
    val cocktailName: LiveData<String> = _cocktailName

    private val _cocktailAlcoholic = MutableLiveData<String>()
    val cocktailAlcoholic: LiveData<String> = _cocktailAlcoholic

    private val _cocktailGlass = MutableLiveData<String>()
    val cocktailGlass: LiveData<String> = _cocktailGlass

    private val _ingredientsList = MutableLiveData<List<Ingredient>>()
    val ingredients: LiveData<List<Ingredient>> = _ingredientsList

    private val _cocktailInstruction = MutableLiveData<String>()
    val cocktailInstruction: LiveData<String> = _cocktailInstruction

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
        _cocktailPicture.value = cocktail.strDrinkThumb
        _cocktailName.value = cocktail.strDrink
        _cocktailAlcoholic.value = cocktail.strAlcoholic
        _cocktailGlass.value = cocktail.strGlass
        _ingredientsList.value = cocktail.createNumberedIngredientsList()
        _cocktailInstruction.value = cocktail.strInstructions

        cocktailId = cocktail.idDrink
    }
}