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

    private val selectedFilterIdList: MutableList<Int?> = mutableListOf(null, null)
    private val drinkFilterTypeList: List<DrinkFilterType> =
        listOf(DrinkFilterType.CATEGORY, DrinkFilterType.ALCOHOL)
    lateinit var selectedFilterTypeList: List<DrinkFilter?>

    private val _alcoholSignLiveData = MutableLiveData<String>()
    val alcoholSignLiveData: LiveData<String> = _alcoholSignLiveData

    private val _categorySignLiveData = MutableLiveData<String>()
    val categorySignLiveData: LiveData<String> = _categorySignLiveData

    private lateinit var changeTextSuffix: String

    fun setInitialText(chooseText: String, changeText:String) {
        changeTextSuffix = changeText

        _alcoholSignLiveData.value = chooseText
        _categorySignLiveData.value = chooseText
    }

    fun alcoholFilterSpecified(itemId: Int) {
        selectedFilterIdList[0] = itemId
        _alcoholSignLiveData.value =
            AlcoholDrinkFilter.values()[itemId].key + "   " + changeTextSuffix
    }

    fun categoryFilterSpecified(itemId: Int) {
        selectedFilterIdList[1] = itemId
        _categorySignLiveData.value =
            CategoryDrinkFilter.values()[itemId].key + "   " + changeTextSuffix
    }

    fun onResetButtonClicked() {
        _clearFilterEventLiveData.value = Event(Unit)
    }

    fun onApplyButtonClicked() {
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
            if (id != null) selectedFilterType[id]
            else null
        }

        _applyFilterEventLiveData.value = Event(selectedFilterTypeList)
    }
}