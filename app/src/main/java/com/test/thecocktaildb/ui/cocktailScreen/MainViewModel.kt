package com.test.thecocktaildb.ui.cocktailScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.test.thecocktaildb.dataNew.repository.source.CocktailRepository
import com.test.thecocktaildb.presentationNew.mapper.CocktailModelMapper
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailModel
import com.test.thecocktaildb.ui.base.BaseViewModel
import com.test.thecocktaildb.util.CocktailOfTheDay
import com.test.thecocktaildb.util.Event
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainViewModel(
    stateHandle: SavedStateHandle,
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper,
) : BaseViewModel(stateHandle) {

    private val cocktailOfTheDayId = CocktailOfTheDay().getCocktailId()

    private val _cocktailDetailsEventLiveData = MutableLiveData<Event<Pair<String, Long>>>()
    val cocktailDetailsEventLiveData: LiveData<Event<Pair<String, Long>>> =
        _cocktailDetailsEventLiveData

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    fun openCocktail() {
        launchRequest {
            val cocktailOfTheDay = cocktailRepo
                .findAndAddCocktailById(cocktailOfTheDayId.toLong())
                ?.run(cocktailMapper::mapTo)!!

            withContext(Dispatchers.Main) {
                navigateToCocktailDetailsFragment(cocktailOfTheDay)
            }
        }
    }

    private fun navigateToCocktailDetailsFragment(cocktail: CocktailModel) {
        _cocktailDetailsEventLiveData.value =
            Event(Pair(cocktail.names.defaults ?: "", cocktail.id))
    }
}