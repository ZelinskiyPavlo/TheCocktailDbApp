package com.test.thecocktaildb.ui.cocktailsScreen.favoriteScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.cocktailsScreen.AdapterHandler
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilterType
import com.test.thecocktaildb.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel(), AdapterHandler {

    private val _items = MutableLiveData<List<Cocktail>>().apply { value = emptyList() }
    val items: LiveData<List<Cocktail>> = _items

    private val _cocktailDetailsEvent = MutableLiveData<Event<Pair<String, String>>>()
    val cocktailDetailsEvent: LiveData<Event<Pair<String, String>>> = _cocktailDetailsEvent

    val isSearchResultEmpty: LiveData<Boolean> = Transformations.map(_items) { it.isEmpty() }

    private var allCocktailList: List<Cocktail>? = null

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

//    TODO: add filter by favorite
    fun loadFavoriteCocktails() {
        disposable.add(
            repository.getCocktails().subscribeBy(onSuccess = { cocktailsList ->
                _items.value = cocktailsList
            }, onError = { Timber.e("Error occurred when loading cocktails, $it") })
        )
    }

    override fun updateCocktailAndNavigateDetailsFragment(cocktail: Cocktail) {
        cocktail.dateAdded = Calendar.getInstance().time
        disposable.add(
            repository.saveCocktail(cocktail)
                .subscribeBy(onComplete = {
                    navigateToCocktailDetailsFragment(cocktail)
                    Timber.d("Cocktail with date updated saved, switching to another fragment")
                }, onError = { Timber.e("Error occurred when updating cocktail, $it") })
        )
    }

    private fun navigateToCocktailDetailsFragment(cocktail: Cocktail) {
        _cocktailDetailsEvent.value = Event(Pair(cocktail.strDrink, cocktail.idDrink))
    }

    fun applyFilter(filterTypeList: List<DrinkFilter?>) {
        if (allCocktailList == null) allCocktailList = _items.value

        _items.value = allCocktailList

        filterTypeList.forEach { drinkFilter ->
            if (drinkFilter == null) {
                _items.value = allCocktailList
            } else {
                when (drinkFilter.type) {
                    DrinkFilterType.ALCOHOL -> {
                        _items.value = _items.value?.filter { it.strAlcoholic == drinkFilter.key }
                    }
                    DrinkFilterType.CATEGORY -> {
                        _items.value = _items.value?.filter { it.strCategory == drinkFilter.key }
                    }
                    else -> {
                    }
                }
            }
        }
    }
}