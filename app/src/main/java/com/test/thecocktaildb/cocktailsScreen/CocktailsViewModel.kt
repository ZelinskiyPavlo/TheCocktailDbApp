package com.test.thecocktaildb.cocktailsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.utils.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class CocktailsViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel() {

    private val _items = MutableLiveData<List<Cocktail>>().apply { value = emptyList() }
    val items: LiveData<List<Cocktail>> = _items

    private val _cocktailDetailsEvent = MutableLiveData<Event<Pair<String, String>>>()
    val cocktailDetailsEvent: LiveData<Event<Pair<String, String>>> = _cocktailDetailsEvent

    val isSearchResultEmpty: LiveData<Boolean> = Transformations.map(_items) { it.isEmpty() }

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    fun loadCocktails() {
        disposable.add(
            repository.getCocktails().subscribeBy(onSuccess = { cocktailsList ->
                _items.value = cocktailsList
            }, onError = { Timber.e("Error occurred when loading cocktails, $it") })
        )
    }

    fun updateCocktailAndNavigateDetailsFragment(cocktail: Cocktail) {
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
}