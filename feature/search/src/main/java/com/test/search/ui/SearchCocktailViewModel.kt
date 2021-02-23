package com.test.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import com.test.common.Event
import com.test.presentation.extension.debounce
import com.test.presentation.mapper.cocktail.CocktailModelMapper
import com.test.presentation.model.cocktail.CocktailModel
import com.test.presentation.ui.base.BaseViewModel
import com.test.repository.source.CocktailRepository
import kotlinx.coroutines.Job

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

    private var searchJob: Job? = null

    val searchQueryLiveData = MutableLiveData<String>()
    private val _searchQueryLiveData: LiveData<String?> = searchQueryLiveData.debounce(550L)

    val isSearchQueryEmptyLiveData: LiveData<Boolean> =
        _itemsLiveData.map { it.isNullOrEmpty() && _searchQueryLiveData.value.isNullOrEmpty() }

    val isSearchResultEmptyLiveData: LiveData<Boolean> =
        _itemsLiveData.map { it.isNullOrEmpty() && _searchQueryLiveData.value.isNullOrEmpty().not() }

    init {
        _searchQueryLiveData.observeForever {
            if (it.isNullOrBlank()) _itemsLiveData.value = emptyList()
            else performSearch(it)
        }
    }

    private fun performSearch(query: String) {
        if (searchJob?.isActive == true) searchJob?.cancel()

        searchJob = launchRequest(_itemsLiveData) {
            cocktailRepo.searchCocktails(query)?.map(cocktailMapper::mapTo) ?: emptyList()
        }
    }

    fun saveCocktailAndNavigateDetailsFragment(cocktail: CocktailModel) {
        launchRequest {
            saveCocktailToDb(cocktail)
            navigateToCocktailDetailsFragment(cocktail)
        }
    }

    private suspend fun saveCocktailToDb(cocktail: CocktailModel) {
        cocktailRepo.addOrReplaceCocktail(cocktail.run(cocktailMapper::mapFrom))
    }

    private fun navigateToCocktailDetailsFragment(cocktail: CocktailModel) {
        _cocktailDetailsEventLiveData
            .postValue(Event(Pair(cocktail.names.defaults ?: "", cocktail.id)))
    }
}