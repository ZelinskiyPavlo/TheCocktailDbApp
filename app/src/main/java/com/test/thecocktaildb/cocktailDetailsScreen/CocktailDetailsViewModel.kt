package com.test.thecocktaildb.cocktailDetailsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.data.Cocktail
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class CocktailDetailsViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel() {

// TODO: add on BackStack navigation to pasting previous query in search field

    private val _cocktailPicture = MutableLiveData<String>()
    val cocktailPicture: LiveData<String> = _cocktailPicture

    private val _cocktailName = MutableLiveData<String>()
    val cocktailName: LiveData<String> = _cocktailName

    private val _cocktailAlcoholic = MutableLiveData<String>()
    val cocktailAlcoholic: LiveData<String> = _cocktailAlcoholic

    private val _cocktailGlass = MutableLiveData<String>()
    val cocktailGlass: LiveData<String> = _cocktailGlass

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    fun getCocktailById(cocktailId: String) {
        disposable.add(
            repository.getCocktails(cocktailId).subscribeBy(onSuccess = {fillCocktailDetails(it)})
        )
    }

    private fun fillCocktailDetails(cocktail: Cocktail) {
        _cocktailPicture.value = cocktail.strDrinkThumb
        _cocktailName.value = cocktail.strDrink
        _cocktailAlcoholic.value = cocktail.strAlcoholic
        _cocktailGlass.value = cocktail.strGlass
    }
}