package com.test.thecocktaildb.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import com.test.thecocktaildb.dataNew.repository.source.CocktailRepository
import com.test.thecocktaildb.presentationNew.mapper.CocktailModelMapper
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailModel
import com.test.thecocktaildb.ui.base.BaseViewModel
import com.test.thecocktaildb.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchCocktailViewModel(
    stateHandle: SavedStateHandle,
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper
) : BaseViewModel(stateHandle) {

    private val _itemsLiveData = MutableLiveData<List<CocktailModel>>().apply { value = emptyList() }
    val itemsLiveData: LiveData<List<CocktailModel>> = _itemsLiveData

    private val _cocktailDetailsEventLiveData = MutableLiveData<Event<Pair<String, Long>>>()
    val cocktailDetailsEventLiveData: LiveData<Event<Pair<String, Long>>> =
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
        launchRequest(_itemsLiveData) {
            cocktailRepo.searchCocktails(query)?.map(cocktailMapper::mapTo) ?: emptyList()
        }
    }

    fun saveCocktailAndNavigateDetailsFragment(cocktail: CocktailModel) {
        launchRequest {
            cocktailRepo.addOrReplaceCocktail(cocktail.run(cocktailMapper::mapFrom))
            navigateToCocktailDetailsFragment(cocktail)
        }
    }

    private fun navigateToCocktailDetailsFragment(cocktail: CocktailModel) {
        _cocktailDetailsEventLiveData
            .postValue(Event(Pair(cocktail.names.defaults ?: "", cocktail.id)))
    }
}