package com.test.thecocktaildb.ui.cocktailScreen.fragmentHostScreen

import androidx.lifecycle.*
import com.test.thecocktaildb.dataNew.repository.source.CocktailRepository
import com.test.thecocktaildb.presentationNew.mapper.CocktailModelMapper
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailAlcoholType
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailCategory
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailModel
import com.test.thecocktaildb.ui.base.BaseViewModel
import com.test.thecocktaildb.ui.cocktailScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailScreen.drinkFilter.DrinkFilterType
import com.test.thecocktaildb.ui.cocktailScreen.sortType.CocktailSortType
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

    var isFilterFragmentOpened = false

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

    private lateinit var changeTextSuffix: String
    private lateinit var chooseTextSuffix: String

    private var cachedSortingOrder: CocktailSortType? = null

    private var sortingOrderIndex by stateHandle<Int?>()
    private var filterTypeIndexArray by stateHandle<Array<String?>>()

    private val filterAndSortLiveData: LiveData<Unit> = MediatorLiveData<Unit>().apply {
        fun transformData() {
            allCocktailList?.let { _cocktailsLiveData.value = it }
            applyFilter(_filterListLiveData.value ?: listOf(null, null))
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
                    else -> throw IllegalArgumentException("unknown filter type was chosen")
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

    val filterResultLiveData: LiveData<String> =
        MediatorLiveData<String>().apply {
            fun determineResult() {
                if (!isFilterFragmentOpened) return
                if (_filterListLiveData.value == listOf(null, null)) return

                val numberOfHistory = cocktailsLiveData.value?.size
                val numberOfFavorite = favoriteListLiveData.value?.size

                value = if (numberOfHistory == 0 && numberOfFavorite == 0)
                    "Результаті відсутні"
                else
                    "Результати (${numberOfHistory ?: 0}⌚, ${numberOfFavorite ?: 0}♥)"
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
                _filterListLiveData.value = listOf(null, null)
                filterTypeIndexArray = arrayOfNulls<String?>(2)
            } else {
                val extractedFilterList = filterTypeIndexArray!!
                    .mapIndexed { index, filterTypeKey ->
                        when {
                            filterTypeKey == null -> null
                            index == 0 -> CocktailAlcoholType.values()
                                .find { it.key == filterTypeKey }
                            index == 1 -> CocktailCategory.values()
                                .find { it.key == filterTypeKey }
                            index == 2 -> throw IndexOutOfBoundsException(
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

    fun setInitialText(chooseText: String, changeText: String) {
        changeTextSuffix = changeText
        chooseTextSuffix = chooseText
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
            else -> throw IllegalArgumentException("unknown filter type was chosen")
        }
    }

    private fun applyFilter(filterTypeList: List<DrinkFilter?>) {
        if (filterTypeList == listOf(null, null)) return

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
                else -> throw IllegalArgumentException("unknown filter type was chosen")
            }
        }
    }

    fun resetFilters() {
        _filterListLiveData.value = listOf(null, null)
    }

    fun onApplyButtonClicked() {
        isFilterFragmentOpened = false
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