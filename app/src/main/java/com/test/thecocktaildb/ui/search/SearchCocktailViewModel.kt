package com.test.thecocktaildb.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.data.CocktailsRepository
import com.test.thecocktaildb.ui.base.BaseViewModel
import com.test.thecocktaildb.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class SearchCocktailViewModel (
    stateHandle: SavedStateHandle,
    private val repository: CocktailsRepository) :
    BaseViewModel(stateHandle) {

    private val _itemsLiveData = MutableLiveData<List<Cocktail>>().apply { value = emptyList() }
    val itemsLiveData: LiveData<List<Cocktail>> = _itemsLiveData

    private val _cocktailDetailsEventLiveData = MutableLiveData<Event<Pair<String, String>>>()
    val cocktailDetailsEventLiveData: LiveData<Event<Pair<String, String>>> =
        _cocktailDetailsEventLiveData

    val searchQuerySubject = PublishSubject.create<String>()

    val isSearchResultEmptyLiveData: LiveData<Boolean> =
        Transformations.map(_itemsLiveData) { it.isNullOrEmpty() }

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    fun subscribeToSearchSubject() {
        disposable.add(searchQuerySubject
            .debounce(550, TimeUnit.MILLISECONDS)
            .filter { !it.isBlank() }
            .distinctUntilChanged()
            .subscribeBy(onNext = { performSearch(it) })
        )
    }

    private fun performSearch(query: String) {
        disposable.add(
            repository.searchCocktails(query).subscribeBy(onSuccess = {
                _itemsLiveData.value = it.cocktailsList
                Timber.d("Search performed with query $query")
            }, onError = { Timber.e("Error occurred when searching with $query, $it") })
        )
    }

    fun saveCocktailAndNavigateDetailsFragment(cocktail: Cocktail) {
        cocktail.dateAdded = Calendar.getInstance().time
        disposable.add(
            repository.saveCocktail(cocktail)
                .subscribeBy(onComplete = {
                    navigateToCocktailDetailsFragment(cocktail)
                    Timber.d("Cocktail with date added saved, switching to another fragment")
                }, onError = { Timber.e("Error occurred when updating cocktail, $it") })
        )
    }

    private fun navigateToCocktailDetailsFragment(cocktail: Cocktail) {
        _cocktailDetailsEventLiveData.value = Event(Pair(cocktail.strDrink, cocktail.idDrink))
    }
}