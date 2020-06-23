package com.test.thecocktaildb.ui.cocktailsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilterType
import com.test.thecocktaildb.util.BatteryStateCacheHolder
import com.test.thecocktaildb.util.BatteryStateHolder
import com.test.thecocktaildb.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class CocktailsViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel() {

    private val _items = MutableLiveData<List<Cocktail>>().apply { value = emptyList() }
    val items: LiveData<List<Cocktail>> = _items

    private val _cocktailDetailsEvent = MutableLiveData<Event<Pair<String, String>>>()
    val cocktailDetailsEvent: LiveData<Event<Pair<String, String>>> = _cocktailDetailsEvent

    val isSearchResultEmpty: LiveData<Boolean> = Transformations.map(_items) { it.isEmpty() }

    private val _batteryPercent = MutableLiveData<String>()
    val batteryPercent: LiveData<String> = _batteryPercent

    private val _batteryStatus = MutableLiveData<Boolean>()
    val batteryStatus: LiveData<String> =
        Transformations.map(_batteryStatus) { if (it) "BATTERY_OKAY" else "BATTERY_LOW" }

    private val _isBatteryCharging = MutableLiveData<Boolean>()
    val isBatteryCharging: LiveData<Boolean> = _isBatteryCharging

    private val batteryStateCache = BatteryStateCacheHolder()

    private var allCocktailList: List<Cocktail>? = null

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    fun loadCocktails() {
        disposable.add(
            repository.getCocktails().subscribeBy(onSuccess = { cocktailsList ->
                _items.value = cocktailsList
            }, onError = { Timber.e("Error occurred when loading cocktails, $it") })
        )
    }

    fun updateCocktailAndNavigateDetailsFragment(cocktail: Cocktail) {
        cocktail.dateAdded = Calendar.getInstance().time
        disposable.add(
            repository.saveCocktail(cocktail)
                .subscribeBy(onComplete = {
                    navigateToCocktailDetailsFragment(cocktail)
                    Timber.d("Cocktail with date updated saved, switching to another fragment")
                }, onError = { Timber.e("Error occurred when updating cocktail, $it") })
        )
    }

    private fun navigateToCocktailDetailsFragment(cocktail: Cocktail) {
        _cocktailDetailsEvent.value = Event(Pair(cocktail.strDrink, cocktail.idDrink))
    }

    fun openProposedCocktail(selectedCocktailId: String?) {
        val otherCocktail = items.value
            ?.filter { it.idDrink != selectedCocktailId }?.random()

        if (otherCocktail != null) {
            updateCocktailAndNavigateDetailsFragment(otherCocktail)
        }
    }

    fun updateBatteryState(stateHolder: BatteryStateHolder) {
        fun determineBatteryStatus(): Boolean {
            if (stateHolder.batteryPercent != null && stateHolder.lowBatteryWarningLevel != null) {
                return stateHolder.batteryPercent > stateHolder.lowBatteryWarningLevel
            }
            return false
        }

        fun cacheBatteryState() {
            stateHolder.lowBatteryWarningLevel?.let {
                batteryStateCache.lowBatteryWarningLevel = stateHolder.lowBatteryWarningLevel
            }
            stateHolder.batteryStatus?.let {
                batteryStateCache.batteryStatus = stateHolder.batteryStatus
            }
            stateHolder.batteryPercent?.let {
                batteryStateCache.batteryPercent = stateHolder.batteryPercent
            }
            stateHolder.isCharging?.let {
                batteryStateCache.isCharging = stateHolder.isCharging
            }
        }

        cacheBatteryState()

        if (batteryStateCache.batteryStatus == null) {
            _batteryStatus.value = determineBatteryStatus()
        } else {
            _batteryStatus.value = batteryStateCache.batteryStatus
        }
        _isBatteryCharging.value = batteryStateCache.isCharging ?: false
        _batteryPercent.value = batteryStateCache.batteryPercent.toString()
    }

    fun applyFilter(filterTypeList: List<DrinkFilter?>) {
        if (allCocktailList == null) allCocktailList = _items.value

        _items.value = allCocktailList

        filterTypeList.forEach { drinkFilter ->
            if (drinkFilter == null) {
                _items.value = allCocktailList
            } else {
                when (drinkFilter.type) {
                    DrinkFilterType.ALCOHOL -> {
                        _items.value = _items.value?.filter { it.strAlcoholic == drinkFilter.key }
                    }
                    DrinkFilterType.CATEGORY -> {
                        _items.value = _items.value?.filter { it.strCategory == drinkFilter.key }
                    }
                    else -> {
                    }
                }
            }
        }
    }
}
