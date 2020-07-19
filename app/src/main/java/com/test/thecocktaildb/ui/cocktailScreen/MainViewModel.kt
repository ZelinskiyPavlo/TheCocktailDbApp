package com.test.thecocktaildb.ui.cocktailScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.test.thecocktaildb.data.CocktailsRepository
import com.test.thecocktaildb.dataNew.repository.source.CocktailRepository
import com.test.thecocktaildb.presentationNew.mapper.CocktailMapper
import com.test.thecocktaildb.presentationNew.mapper.CocktailModelMapper
import com.test.thecocktaildb.presentationNew.model.CocktailModel
import com.test.thecocktaildb.ui.base.BaseViewModel
import com.test.thecocktaildb.util.CocktailOfTheDay
import com.test.thecocktaildb.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class MainViewModel(
    stateHandle: SavedStateHandle,
    private val repository: CocktailsRepository,
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper,
    private val oldCocktailMapper: CocktailMapper
) : BaseViewModel(stateHandle) {

    private val cocktailOfTheDayId = CocktailOfTheDay().getCocktailId()

    private val _cocktailDetailsEventLiveData = MutableLiveData<Event<Pair<String, Long>>>()
    val cocktailDetailsEventLiveData: LiveData<Event<Pair<String, Long>>> =
        _cocktailDetailsEventLiveData

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    fun openCocktail() {
        disposable.add(
            repository.findCocktailById(cocktailOfTheDayId.toString())
                .map {
                    val cocktail = it.cocktailsList!!.first()
                    cocktail
                }.subscribeBy(onSuccess = {cocktail ->
                    val cocktailModel = cocktail.run(oldCocktailMapper::mapFrom)
                    launchRequest {
                        cocktailRepo.addOrReplaceCocktail(cocktailModel.run(cocktailMapper::mapFrom))
                    }
                    navigateToCocktailDetailsFragment(cocktailModel)
                }, onError = { Timber.e("Error during getting cocktail of the day $it") })
        )
    }

    private fun navigateToCocktailDetailsFragment(cocktail: CocktailModel) {
        _cocktailDetailsEventLiveData.value = Event(Pair(cocktail.names.defaults ?: "", cocktail.id))
    }
}