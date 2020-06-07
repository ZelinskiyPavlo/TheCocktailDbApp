package com.test.thecocktaildb.ui.searchCocktailsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchCocktailsViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel() {

    private val _items = MutableLiveData<List<Cocktail>>().apply { value = emptyList() }
    val items: LiveData<List<Cocktail>> = _items

    private val _cocktailDetailsEvent = MutableLiveData<Event<Pair<String, String>>>()
    val cocktailDetailsEvent: LiveData<Event<Pair<String, String>>> = _cocktailDetailsEvent

    val searchQuerySubject = PublishSubject.create<String>()

    val isSearchResultEmpty: LiveData<Boolean> = Transformations.map(_items) { it.isNullOrEmpty() }

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
                _items.value = it.cocktailsList
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
        _cocktailDetailsEvent.value = Event(Pair(cocktail.strDrink, cocktail.idDrink))
    }
}