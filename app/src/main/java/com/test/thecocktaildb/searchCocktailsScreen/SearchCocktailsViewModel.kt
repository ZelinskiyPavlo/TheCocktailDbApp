package com.test.thecocktaildb.searchCocktailsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.utils.Event
import com.test.thecocktaildb.utils.log
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchCocktailsViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel() {

    private val _items = MutableLiveData<List<Cocktail>>().apply { value = emptyList() }
    val items: LiveData<List<Cocktail>> = _items

    private val _cocktailDetailsEvent = MutableLiveData<Event<Pair<String, String>>>()
    val cocktailDetailsEvent: LiveData<Event<Pair<String, String>>> = _cocktailDetailsEvent

    //    Ми використовуємо PublishSubject тому, що в RxJava це аналог змінної. І ця змінна напевно
//    мала б зберігати останній введений результат (по ідеї)
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
            })
        )
    }

    fun performSwitchingToCocktailDetailsFragment(cocktail: Cocktail) {
        val cocktailName = cocktail.strDrink
        val cocktailId = cocktail.idDrink
        disposable.add(
            getNumberOfItemsInDatabase().flatMapSingle { position ->
                cocktail.position = position.toInt()
                Single.just(cocktail)
            }.flatMapCompletable {
                repository.saveCocktail(it)
            }
                .subscribeBy(onComplete = {
                    navigateToCocktailDetailsFragment(cocktailName, cocktailId)
                    Timber.d("Chain completed")
                })
        )
    }

    private fun getNumberOfItemsInDatabase(): Maybe<Long> {
        return repository.getNumberOfItems().firstElement()
    }

    private fun navigateToCocktailDetailsFragment(actionBarTitle: String, cocktailId: String) {
        _cocktailDetailsEvent.postValue(Event(Pair(actionBarTitle, cocktailId)))
    }
}