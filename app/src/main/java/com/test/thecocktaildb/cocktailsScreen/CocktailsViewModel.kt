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
import javax.inject.Inject

class CocktailsViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel() {

    private val _items = MutableLiveData<List<Cocktail>>().apply { value = emptyList() }
    val items: LiveData<List<Cocktail>> = _items

    private val _cocktailDetailEvent = MutableLiveData<Event<Pair<String, String>>>()
    val cocktailDetailEvent: LiveData<Event<Pair<String, String>>> = _cocktailDetailEvent

    val isSearchResultEmpty: LiveData<Boolean> = Transformations.map(_items) { it.isEmpty() }

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    fun loadCocktails() {

    }

    fun navigateToCocktailDetailsFragment(cocktail: Cocktail) {
        _cocktailDetailEvent.value = Event(Pair(cocktail.strDrink, cocktail.idDrink))
    }
}