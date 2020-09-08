package com.test.thecocktaildb.presentation.ui.cocktail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.test.thecocktaildb.data.repository.source.CocktailRepository
import com.test.thecocktaildb.presentation.mapper.CocktailModelMapper
import com.test.thecocktaildb.presentation.model.cocktail.CocktailModel
import com.test.thecocktaildb.presentation.ui.base.BaseViewModel
import com.test.thecocktaildb.util.CocktailOfTheDay
import com.test.thecocktaildb.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainViewModel(
    stateHandle: SavedStateHandle,
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper,
) : BaseViewModel(stateHandle) {

    private val cocktailOfTheDayId = CocktailOfTheDay().getCocktailId()

    private val _cocktailDetailsEventLiveData = MutableLiveData<Event<Pair<String, Long>>>()
    val cocktailDetailsEventLiveData: LiveData<Event<Pair<String, Long>>> =
        _cocktailDetailsEventLiveData

    private val _showNoCocktailFoundDialogEventLiveData = MutableLiveData<Event<String>>()
    val showNoCocktailFoundDialogEventLiveData: LiveData<Event<String>> =
        _showNoCocktailFoundDialogEventLiveData

    fun openCocktailOfTheDay() {
        launchRequest {
            val cocktailOfTheDay = cocktailRepo
                .findAndAddCocktailById(cocktailOfTheDayId.toLong())
                ?.run(cocktailMapper::mapTo)!!

            withContext(Dispatchers.Main) {
                navigateToCocktailDetailsFragment(cocktailOfTheDay)
            }
        }
    }

    fun openCocktailById(id: Long) {
        Timber.i("openCocktailById called $id")
        launchRequest {
            val cocktail = cocktailRepo.getCocktailById(id)?.run(cocktailMapper::mapTo)
                ?: cocktailRepo.findAndAddCocktailById(id)?.run(cocktailMapper::mapTo)
            if (cocktail == null) {
                Timber.i("cocktail == null")
                withContext(Dispatchers.Main) {
                    _showNoCocktailFoundDialogEventLiveData.value = Event(id.toString())
                }
                return@launchRequest
            }

            withContext(Dispatchers.Main) {
                Timber.i("navigateToCocktailDetailsFragment")
                navigateToCocktailDetailsFragment(cocktail)
            }
        }
    }

    private fun navigateToCocktailDetailsFragment(cocktail: CocktailModel) {
        _cocktailDetailsEventLiveData.value =
            Event(Pair(cocktail.names.defaults ?: "", cocktail.id))
    }
}