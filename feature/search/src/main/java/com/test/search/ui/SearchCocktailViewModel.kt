package com.test.search.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.test.presentation.mapper.cocktail.CocktailModelMapper
import com.test.presentation.model.cocktail.CocktailModel
import com.test.presentation.ui.base.BaseViewModel
import com.test.presentation.util.WhileViewSubscribed
import com.test.repository.source.CocktailRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchCocktailViewModel(
    stateHandle: SavedStateHandle,
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper
) : BaseViewModel(stateHandle) {

    sealed class Event {
        class ToDetails(val cocktailId: Long) : Event()
    }

    private val _eventsChannel = Channel<Event>(capacity = Channel.CONFLATED)
    val eventsFlow = _eventsChannel.receiveAsFlow()

    private val _itemsFlow = MutableSharedFlow<List<CocktailModel>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val itemsFlow = _itemsFlow
        .stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    val searchQueryFlow = MutableStateFlow("")

    val isSearchQueryEmptyFlow = _itemsFlow.map {
        it.isEmpty() && searchQueryFlow.value.isEmpty()
    }

    val isSearchResultEmptyFlow = _itemsFlow.map {
        it.isEmpty() && searchQueryFlow.value.isEmpty().not()
    }

    private var searchJob: Job? = null

    init {
        // set initial value to sharedFlow to trigger dependent flows
        _itemsFlow.tryEmit(emptyList())

        viewModelScope.launch {
            searchQueryFlow.collect {
                performSearch(it)
            }
        }
    }

    private fun performSearch(query: String) {
        searchJob?.cancel()

        if (query.isEmpty()) {
            _itemsFlow.tryEmit(emptyList())
            return
        }

        searchJob = launchRequest {
            // Simulating debounce to avoid using built function, bc of FlowPreview annotation
            delay(500)
            _itemsFlow.emit(
                cocktailRepo.searchCocktails(query)?.map(cocktailMapper::mapTo)
                    ?: emptyList()
            )
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
        _eventsChannel.trySend(Event.ToDetails(cocktail.id))
    }
}