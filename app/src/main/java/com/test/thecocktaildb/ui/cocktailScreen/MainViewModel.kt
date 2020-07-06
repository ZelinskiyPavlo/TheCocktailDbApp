package com.test.thecocktaildb.ui.cocktailScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.util.CocktailOfTheDay
import com.test.thecocktaildb.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel() {

    private val cocktailOfTheDayId = CocktailOfTheDay().getCocktailId()

    private val _cocktailDetailsEventLiveData = MutableLiveData<Event<Pair<String, String>>>()
    val cocktailDetailsEventLiveData: LiveData<Event<Pair<String, String>>> =
        _cocktailDetailsEventLiveData

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    fun openCocktail() {
        disposable.add(
            repository.findCocktailById(cocktailOfTheDayId.toString())
                .map {
                    val cocktail = it.cocktailsList!!.first()
                    cocktail.dateAdded = Calendar.getInstance().time
                    repository.saveCocktail(cocktail)
                        .subscribeBy(onError = { error ->
                            Timber.e("Error during saving cocktail of the day to DB, $error")
                        })
                    cocktail
                }.subscribeBy(onSuccess = {
                    navigateToCocktailDetailsFragment(it)
                }, onError = { Timber.e("Error during getting cocktail of the day $it") })
        )
    }

    private fun navigateToCocktailDetailsFragment(cocktail: Cocktail) {
        _cocktailDetailsEventLiveData.value = Event(Pair(cocktail.strDrink, cocktail.idDrink))
    }
}