package com.test.thecocktaildb.ui.cocktailsScreen.filterScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.AlcoholDrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.CategoryDrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilterType
import com.test.thecocktaildb.util.Event
import javax.inject.Inject

class CocktailFilterViewModel @Inject constructor() : ViewModel() {

    private val _applyFilterEventLiveData = MutableLiveData<Event<List<DrinkFilter?>>>()
    val applyFilterEventLiveData: LiveData<Event<List<DrinkFilter?>>> = _applyFilterEventLiveData

    private val _clearFilterEventLiveData = MutableLiveData<Event<Unit>>()
    val clearFilterEventLiveData: LiveData<Event<Unit>> = _clearFilterEventLiveData

    private val _retrieveFilterEventLiveData = MutableLiveData<Event<Unit>>()
    val retrieveFilterEventLiveData: LiveData<Event<Unit>> = _retrieveFilterEventLiveData

    var numberOfRadioButtons = 0
    lateinit var selectedFilterViewIdList: List<Int>
    lateinit var drinkFilterTypeList: List<DrinkFilterType?>
    lateinit var selectedFilterTypeList: List<DrinkFilter?>

    fun onResetButtonClicked() {
        _clearFilterEventLiveData.value = Event(Unit)
    }

    fun onApplyButtonClicked() {
        _retrieveFilterEventLiveData.value = Event(Unit)

        val filterTypeSizeList = drinkFilterTypeList.filterNotNull().map { drinkFilterType ->
            when (drinkFilterType) {
                DrinkFilterType.ALCOHOL -> AlcoholDrinkFilter.values().size
                DrinkFilterType.CATEGORY -> CategoryDrinkFilter.values().size
                else -> throw IllegalArgumentException("Unknown enum specified")
            }
        }

        val selectedFilterIdList = selectedFilterViewIdList.mapIndexed { index, viewId ->
            if (viewId == -1) return@mapIndexed -1

            if (index == 0 && viewId == filterTypeSizeList[index]) return@mapIndexed -1

            if (index == 1 && viewId == numberOfRadioButtons - 1) return@mapIndexed -1

            if (index != 1) Math.floorMod(viewId, filterTypeSizeList[index])
            else Math.floorMod(viewId, numberOfRadioButtons - filterTypeSizeList[index - 1] - 1)
        }

        selectedFilterTypeList = selectedFilterIdList.mapIndexed { index, id ->
            val selectedFilterType: Array<out DrinkFilter> = when (drinkFilterTypeList[index]) {
                DrinkFilterType.ALCOHOL -> {
                    AlcoholDrinkFilter.values()
                }
                DrinkFilterType.CATEGORY -> {
                    CategoryDrinkFilter.values()
                }
                else -> throw IllegalArgumentException("Unknown enum specified")
            }
            if (selectedFilterIdList[index] != -1) selectedFilterType[id]
            else null
        }

        _applyFilterEventLiveData.value = Event(selectedFilterTypeList)
    }
}