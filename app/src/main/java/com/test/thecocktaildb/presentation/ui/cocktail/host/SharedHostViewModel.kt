package com.test.thecocktaildb.presentation.ui.cocktail.host

import androidx.lifecycle.*
import com.test.thecocktaildb.data.repository.source.CocktailRepository
import com.test.thecocktaildb.presentation.mapper.CocktailModelMapper
import com.test.thecocktaildb.presentation.model.cocktail.CocktailAlcoholType
import com.test.thecocktaildb.presentation.model.cocktail.CocktailCategory
import com.test.thecocktaildb.presentation.model.cocktail.CocktailModel
import com.test.thecocktaildb.presentation.ui.base.BaseViewModel
import com.test.thecocktaildb.presentation.ui.cocktail.filtertype.DrinkFilter
import com.test.thecocktaildb.presentation.ui.cocktail.filtertype.DrinkFilterType
import com.test.thecocktaildb.presentation.ui.cocktail.filtertype.IngredientDrinkFilter
import com.test.thecocktaildb.presentation.ui.cocktail.sorttype.CocktailSortType
import com.test.thecocktaildb.util.Event
import com.test.thecocktaildb.util.stateHandle
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SharedHostViewModel(
    handle: SavedStateHandle,
    private val cocktailRepo: CocktailRepository,
    private val cocktailMapper: CocktailModelMapper
) : BaseViewModel(handle) {

    private val _cocktailsLiveData =
        MutableLiveData<List<CocktailModel>>().apply { value = emptyList() }
    val cocktailsLiveData: LiveData<List<CocktailModel>> = _cocktailsLiveData

    val favoriteListLiveData: LiveData<List<CocktailModel>> =
        Transformations.map(_cocktailsLiveData) {
            it.filter { cocktail -> cocktail.isFavorite }
        }

    private var allCocktailList: List<CocktailModel>? = null

    private val cocktailDbListLiveData: LiveData<List<CocktailModel>> =
        cocktailRepo.cocktailListLiveData.map(cocktailMapper::mapToList)

    private val _cocktailDetailsEventLiveData = MutableLiveData<Event<Pair<String, Long>>>()
    val cocktailDetailsEventLiveData: LiveData<Event<Pair<String, Long>>> =
        _cocktailDetailsEventLiveData

    private val _applyFilterEventLiveData = MutableLiveData<Event<Unit>>()
    val applyFilterEventLiveData: LiveData<Event<Unit>> = _applyFilterEventLiveData

    val isCocktailListEmptyLiveData: LiveData<Boolean> =
        Transformations.map(_cocktailsLiveData) { it.isEmpty() }

    val isFavoriteListEmptyLiveData: LiveData<Boolean> =
        Transformations.map(favoriteListLiveData) { it.isEmpty() }

    private val _filterListLiveData = MutableLiveData<List<DrinkFilter?>>()
    val filterListLiveData: LiveData<List<DrinkFilter?>> = _filterListLiveData

    var sortingOrderLiveData = MutableLiveData<CocktailSortType?>()

    val alcoholSignLiveData: LiveData<String> = Transformations.map(_filterListLiveData) {
        if (it[0] == null) chooseTextSuffix
        else "${it[0]?.key}    $changeTextSuffix"
    }

    val categorySignLiveData: LiveData<String> = Transformations.map(_filterListLiveData) {
        if (it[1] == null) chooseTextSuffix
        else "${it[1]?.key?.replace("\\/", "")}    $changeTextSuffix"
    }

    val ingredientSignLiveData: LiveData<String> = Transformations.map(_filterListLiveData) {
        if (it[2] == null) chooseTextSuffix
        else "${it[2]?.key?.replace("\\/", "")}    $changeTextSuffix"
    }

    private lateinit var changeTextSuffix: String
    private lateinit var chooseTextSuffix: String
    private lateinit var emptyResult: String

    private var cachedSortingOrder: CocktailSortType? = null

    private var sortingOrderIndex by stateHandle<Int?>()
    private var filterTypeIndexArray by stateHandle<Array<String?>>()

    private val filterAndSortLiveData: LiveData<Unit> = MediatorLiveData<Unit>().apply {
        fun transformData() {
            allCocktailList?.let { _cocktailsLiveData.value = it }
            applyFilter(_filterListLiveData.value ?: listOf(null, null, null))
            applySorting(sortingOrderLiveData.value)
            value = Unit
        }

        fun saveFilters() {
            _filterListLiveData.value?.forEachIndexed { index, filterType ->
                filterTypeIndexArray = when {
                    filterType == null -> {
                        filterTypeIndexArray?.set(index, null)
                        filterTypeIndexArray
                    }
                    filterType.type == DrinkFilterType.ALCOHOL -> {
                        filterTypeIndexArray?.set(0, filterType.key)
                        filterTypeIndexArray
                    }
                    filterType.type == DrinkFilterType.CATEGORY -> {
                        filterTypeIndexArray?.set(1, filterType.key)
                        filterTypeIndexArray
                    }
                    filterType.type == DrinkFilterType.INGREDIENT -> {
                        filterTypeIndexArray?.set(2, filterType.key)
                        filterTypeIndexArray
                    }
                    else -> throw IllegalArgumentException("Unknown filter type was chosen")
                }
            }
        }

        fun saveSorting() {
            sortingOrderIndex = sortingOrderLiveData.value?.ordinal ?: 0
        }

        addSource(_filterListLiveData) {
            transformData()
            saveFilters()
        }

        addSource(sortingOrderLiveData) {
            transformData()
            saveSorting()
        }

        addSource(cocktailDbListLiveData) {
            cocktailDbListLiveData.value?.let { cocktailsList ->
                _cocktailsLiveData.value = cocktailsList
                allCocktailList = cocktailsList
                transformData()
            }
        }
    }

    val filterResultLiveData: LiveData<Event<String>> =
        MediatorLiveData<Event<String>>().apply {
            fun determineResult() {
                if (_filterListLiveData.value == listOf(null, null, null)) return

                val numberOfHistory = cocktailsLiveData.value?.size
                val numberOfFavorite = favoriteListLiveData.value?.size

                value = if (numberOfHistory == 0 && numberOfFavorite == 0)
                    Event(emptyResult)
                else
                    Event("Results (${numberOfHistory ?: 0}⌚, ${numberOfFavorite ?: 0}♥)")
            }
            addSource(filterAndSortLiveData) {
                determineResult()
            }
        }

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    init {
        fun restoreSortingOrder() {
            sortingOrderIndex?.let {
                sortingOrderLiveData.value = CocktailSortType.values()[it]
            }
        }

        @Suppress("IMPLICIT_CAST_TO_ANY")
        fun restoreFilters() {
            if (filterTypeIndexArray == null) {
                _filterListLiveData.value = listOf(null, null, null)
                filterTypeIndexArray = arrayOfNulls<String?>(3)
            } else {
                val extractedFilterList = filterTypeIndexArray!!
                    .mapIndexed { index, filterTypeKey ->
                        when {
                            filterTypeKey == null -> null
                            index == 0 -> CocktailAlcoholType.values()
                                .find { it.key == filterTypeKey }
                            index == 1 -> CocktailCategory.values()
                                .find { it.key == filterTypeKey }
                            index == 2 -> IngredientDrinkFilter.values()
                                .find { it.key == filterTypeKey }
                            index == 3 -> throw IndexOutOfBoundsException(
                                "Looks like you added new filter type" +
                                        " and forget to add extracting it's value"
                            )
                            else -> throw IllegalArgumentException(
                                "Error during extracting values from filterType " +
                                        "array. "
                            )
                        }
                    } as List<DrinkFilter?>
                _filterListLiveData.value = extractedFilterList
            }
        }
        restoreSortingOrder()
        restoreFilters()
    }

    fun updateCocktailAndNavigateDetailsFragment(cocktail: CocktailModel) {
        launchRequest {
            cocktailRepo.updateCocktailDate(cocktail.id)

            withContext(Dispatchers.Main) {
                navigateToCocktailDetailsFragment(cocktail)
            }
        }
    }

    private fun navigateToCocktailDetailsFragment(cocktail: CocktailModel) {
        _cocktailDetailsEventLiveData.value =
            Event(Pair(cocktail.names.defaults ?: "", cocktail.id))
    }

    fun openProposedCocktail(selectedCocktailId: Long?) {
        val otherCocktail = cocktailsLiveData.value
            ?.filter { it.id != selectedCocktailId }?.random()

        if (otherCocktail != null) {
            updateCocktailAndNavigateDetailsFragment(otherCocktail)
        }
    }

    fun changeIsFavoriteState(cocktail: CocktailModel) {
        launchRequest {
            cocktailRepo.updateCocktailFavoriteState(cocktail.id, cocktail.isFavorite.not())
        }
    }

    fun setInitialText(chooseText: String, changeText: String, emptyResultText: String) {
        changeTextSuffix = changeText
        chooseTextSuffix = chooseText
        emptyResult = emptyResultText
    }

    fun filterSpecified(itemId: Int, filterType: DrinkFilterType) {
        _filterListLiveData.value = when (filterType) {
            DrinkFilterType.ALCOHOL -> {
                _filterListLiveData.value?.toMutableList()?.apply {
                    set(0, CocktailAlcoholType.values()[itemId])
                }
            }
            DrinkFilterType.CATEGORY -> {
                _filterListLiveData.value?.toMutableList()?.apply {
                    set(1, CocktailCategory.values()[itemId])
                }
            }
            DrinkFilterType.INGREDIENT -> {
                _filterListLiveData.value?.toMutableList()?.apply {
                    set(2, IngredientDrinkFilter.values()[itemId])
                }
            }
            else -> throw IllegalArgumentException("Unknown filter type was chosen")
        }
    }

    private fun applyFilter(filterTypeList: List<DrinkFilter?>) {
        if (filterTypeList == listOf(null, null, null)) return

        filterTypeList.filterNotNull().forEach { filterType ->
            when (filterType.type) {
                DrinkFilterType.ALCOHOL -> {
                    _cocktailsLiveData.value =
                        _cocktailsLiveData.value?.filter { it.alcoholType == filterType }
                }
                DrinkFilterType.CATEGORY -> {
                    _cocktailsLiveData.value =
                        _cocktailsLiveData.value?.filter { it.category == filterType }
                }
                DrinkFilterType.INGREDIENT -> {
                    _cocktailsLiveData.value =
                        _cocktailsLiveData.value?.filter {
                            it.ingredients.contains(filterType)
                        }
                }
                else -> throw IllegalArgumentException("Unknown filter type was chosen")
            }
        }
    }

    fun resetFilters() {
        _filterListLiveData.value = listOf(null, null, null)
    }

    fun onApplyButtonClicked() {
        _applyFilterEventLiveData.value = Event(Unit)
    }

    private fun applySorting(cocktailSortType: CocktailSortType?) {
        cachedSortingOrder = cocktailSortType ?: CocktailSortType.RECENT

        val alcoholDrinkFilter = CocktailAlcoholType.values()
        val alcoholComparator = kotlin.Comparator<CocktailModel> { t, t2 ->
            alcoholDrinkFilter.indexOf(alcoholDrinkFilter.find { it == t.alcoholType }) -
                    alcoholDrinkFilter.indexOf(alcoholDrinkFilter.find { it == t2.alcoholType })
        }

        _cocktailsLiveData.value = when (cachedSortingOrder) {
            CocktailSortType.RECENT ->
                _cocktailsLiveData.value?.sortedByDescending { it.dateAdded }
            CocktailSortType.NAME_DESC ->
                _cocktailsLiveData.value?.sortedByDescending { it.names.defaults }
            CocktailSortType.NAME_ASC ->
                _cocktailsLiveData.value?.sortedBy { it.names.defaults }
            CocktailSortType.ALCOHOL_FIRST ->
                _cocktailsLiveData.value?.sortedWith(nullsLast(alcoholComparator))
            CocktailSortType.NON_ALCOHOL_FIRST ->
                _cocktailsLiveData.value?.sortedWith(nullsLast(alcoholComparator))?.reversed()
            CocktailSortType.INGREDIENT_DESC ->
                _cocktailsLiveData.value?.sortedByDescending { it.ingredients.size }
            CocktailSortType.INGREDIENT_ASC ->
                _cocktailsLiveData.value?.sortedBy { it.ingredients.size }
            null -> _cocktailsLiveData.value?.sortedByDescending { it.dateAdded }
        }
    }
}