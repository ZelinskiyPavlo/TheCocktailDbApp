package com.test.cocktail.ui

import androidx.lifecycle.*
import com.test.cocktail.model.sorttype.CocktailSortType
import com.test.common.Event
import com.test.presentation.adapter.binding.Page
import com.test.presentation.mapper.cocktail.CocktailModelMapper
import com.test.presentation.mapper.user.UserModelMapper
import com.test.presentation.model.cocktail.CocktailModel
import com.test.presentation.model.cocktail.filter.DrinkFilter
import com.test.presentation.model.cocktail.filter.DrinkFilterType
import com.test.presentation.model.cocktail.type.CocktailAlcoholType
import com.test.presentation.model.cocktail.type.CocktailCategory
import com.test.presentation.model.cocktail.type.CocktailGlassType
import com.test.presentation.model.user.UserModel
import com.test.presentation.ui.base.BaseViewModel
import com.test.presentation.util.liveDataStateHandle
import com.test.presentation.util.stateHandle
import com.test.repository.source.CocktailRepository
import com.test.repository.source.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CocktailViewModel(
    handle: SavedStateHandle,
    private val cocktailRepo: CocktailRepository,
    userRepo: UserRepository,
    private val cocktailMapper: CocktailModelMapper,
    private val userMapper: UserModelMapper
) : BaseViewModel(handle) {

    val currentPageLiveData by liveDataStateHandle(initialValue = Page.HistoryPage)

    private val cocktailsDbListLiveData: LiveData<List<CocktailModel>> =
        cocktailRepo.cocktailListLiveData.map(cocktailMapper::mapToList)

    private val userLiveData: LiveData<UserModel?> = userRepo.userLiveData.map {
        it?.run(userMapper::mapTo)
    }

    private var allCocktails: List<CocktailModel>? = null

    private val _cocktailsLiveData =
        MutableLiveData<List<CocktailModel>>().apply { value = emptyList() }
    val cocktailsLiveData: LiveData<List<CocktailModel>> = _cocktailsLiveData

    val favoriteCocktailsLiveData: LiveData<List<CocktailModel>> =
        Transformations.map(_cocktailsLiveData) {
            it.filter { cocktail -> cocktail.isFavorite }
        }

    val isCocktailsEmptyLiveData: LiveData<Boolean> =
        Transformations.map(_cocktailsLiveData) { it.isEmpty() }

    val isFavoriteCocktailsEmptyLiveData: LiveData<Boolean> =
        Transformations.map(favoriteCocktailsLiveData) { it.isEmpty() }

    private val _filtersLiveData = MutableLiveData<List<DrinkFilter?>?>()
    val filtersLiveData: LiveData<List<DrinkFilter?>?> = _filtersLiveData

    internal var sortingOrderLiveData = MutableLiveData<CocktailSortType?>()

    private val _cocktailDetailsEventLiveData =
        MutableLiveData<Event<Long>>()
    val cocktailDetailsEventLiveData: LiveData<Event<Long>> =
        _cocktailDetailsEventLiveData

    private val _applyFiltersEventLiveData =
        MutableLiveData<Event<Pair<List<String>, List<String>>?>>()
    val applyFiltersEventLiveData: LiveData<Event<Pair<List<String>, List<String>>?>> =
        _applyFiltersEventLiveData

    private val _favoriteStateChangedEventLiveData =
        MutableLiveData<Event<Triple<Boolean, String, String>>>()
    val favoriteStateChangedEventLiveData: LiveData<Event<Triple<Boolean, String, String>>> =
        _favoriteStateChangedEventLiveData

    private lateinit var changeTextSuffix: String
    private lateinit var chooseTextSuffix: String
    private lateinit var emptyResultText: String
    private lateinit var resultsSign: String

    val alcoholSignLiveData: LiveData<String> = Transformations.map(_filtersLiveData) {
        if (it?.get(0) == null) chooseTextSuffix
        else "${it[0]?.key}    $changeTextSuffix"
    }

    val categorySignLiveData: LiveData<String> = Transformations.map(_filtersLiveData) {
        if (it?.get(1) == null) chooseTextSuffix
        else "${it[1]?.key?.replace("\\/", "")}    $changeTextSuffix"
    }

    val glassSignLiveData: LiveData<String> = Transformations.map(_filtersLiveData) {
        if (it?.get(2) == null) chooseTextSuffix
        else "${it[2]?.key?.replace("\\/", "")}    $changeTextSuffix"
    }

    private var sortingOrderIndex by stateHandle<Int?>()
    private var filterTypeIndexArray by stateHandle<Array<String?>>()

    private val cocktailsListUpdateObservableLiveData: LiveData<Unit> =
        MediatorLiveData<Unit>().apply {
            fun updateList() {
                allCocktails?.let { _cocktailsLiveData.value = it }
                applyFilter(_filtersLiveData.value)
                applySorting(sortingOrderLiveData.value)
                value = Unit
            }

            addSource(_filtersLiveData) {
                updateList()
            }

            addSource(sortingOrderLiveData) {
                updateList()
            }

            addSource(cocktailsDbListLiveData) {
                cocktailsDbListLiveData.value?.let { cocktailsList ->
                    _cocktailsLiveData.value = cocktailsList
                    allCocktails = cocktailsList
                    updateList()
                }
            }
        }

    val filterResultLiveData: LiveData<Event<String>> = MediatorLiveData<Event<String>>().apply {
        fun determineResult() {
            if (_filtersLiveData.value == null) return

            val numberOfHistory = cocktailsLiveData.value?.size
            val numberOfFavorite = favoriteCocktailsLiveData.value?.size

            value = if (numberOfHistory == 0 && numberOfFavorite == 0)
                Event(emptyResultText)
            else
                Event("$resultsSign (${numberOfHistory ?: 0}⌚, ${numberOfFavorite ?: 0}♥)")
        }
        addSource(cocktailsListUpdateObservableLiveData) {
            determineResult()
        }
    }

    private val emptyObserver = Observer<Any?> {}

    init {
        fun restoreSortingOrder() {
            sortingOrderIndex?.let {
                sortingOrderLiveData.value = CocktailSortType.values()[it]
            }
        }

        @Suppress("IMPLICIT_CAST_TO_ANY")
        fun restoreFilters() {
            if (filterTypeIndexArray == null) {
                _filtersLiveData.value = null
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
                            index == 2 -> CocktailGlassType.values()
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
                _filtersLiveData.value = extractedFilterList
            }
        }
        restoreSortingOrder()
        restoreFilters()

        // TODO: 23.02.2021 It's bad practice. Consider using switchMap for example
        userLiveData.observeForever (emptyObserver)
        cocktailsListUpdateObservableLiveData.observeForever (emptyObserver)
        currentPageLiveData.observeForever (emptyObserver)
    }

    override fun onCleared() {
        fun saveFilters() {
            _filtersLiveData.value?.forEachIndexed { index, filterType ->
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
                    filterType.type == DrinkFilterType.GLASS -> {
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
        /** Is this worth doing it??? Bc after changing scope of viewModel from
         *  activity to CocktailFragment no saving performed after closing app. I think with
         *  fragment scope it will only save data when app is open but you just navigated to
         *  different screen
         */
        saveFilters()
        saveSorting()

        userLiveData.removeObserver (emptyObserver)
        cocktailsListUpdateObservableLiveData.removeObserver (emptyObserver)
        currentPageLiveData.removeObserver (emptyObserver)

        super.onCleared()
    }

    fun setInitialText(chooseText: String, changeText: String, emptyResultText: String, resultsSign: String) {
        changeTextSuffix = changeText
        chooseTextSuffix = chooseText
        this.emptyResultText = emptyResultText
        this.resultsSign = resultsSign
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
        _cocktailDetailsEventLiveData.value = Event(cocktail.id)
    }

    fun changeIsFavoriteState(cocktail: CocktailModel) {
        launchRequest {
            cocktailRepo.updateCocktailFavoriteState(cocktail.id, cocktail.isFavorite.not())

            withContext(Dispatchers.Main) {
                val isAddedToFavorite = cocktail.isFavorite.not()
                val cocktailId = cocktail.id.toString()
                val fullUserName = "${userLiveData.value?.name} ${userLiveData.value?.lastName}"

                _favoriteStateChangedEventLiveData.value =
                    Event(Triple(isAddedToFavorite, cocktailId, fullUserName))
            }
        }
    }

    fun onApplyButtonClicked() {
        val filterList = _filtersLiveData.value
        if (filterList != null || filterList == listOf(null, null, null)) {
            val selectedFiltersList = filterList.map { it?.key ?: "None" }
            val selectedFiltersTypeList = filterList.filterNotNull().map { it.type.name }
            _applyFiltersEventLiveData.value =
                Event(Pair(selectedFiltersList, selectedFiltersTypeList))
        } else {
            _applyFiltersEventLiveData.value = Event(Pair(emptyList(), emptyList()))
        }
    }

    fun openProposedCocktail(selectedCocktailId: Long?) {
        val otherCocktail = cocktailsLiveData.value
            ?.filter { it.id != selectedCocktailId }?.random()

        if (otherCocktail != null) {
            updateCocktailAndNavigateDetailsFragment(otherCocktail)
        }
    }

    fun filterSpecified(itemId: Int, filterType: DrinkFilterType) {
        val selectedFilterEnumList = when (filterType) {
            DrinkFilterType.ALCOHOL -> CocktailAlcoholType.values()
            DrinkFilterType.CATEGORY -> CocktailCategory.values()
            DrinkFilterType.GLASS -> CocktailGlassType.values()
            else -> throw IllegalArgumentException("Unknown filter type was chosen")
        }

        _filtersLiveData.value =
            (_filtersLiveData.value ?: listOf(null, null, null)).toMutableList().apply {
                set(filterType.ordinal, selectedFilterEnumList[itemId])
            }
    }

    fun resetFilters() {
        _filtersLiveData.value = null
    }

    private fun applyFilter(filterTypeList: List<DrinkFilter?>?) {
        if (filterTypeList == null) return

        filterTypeList.filterNotNull().forEach { filterType ->
            _cocktailsLiveData.value = _cocktailsLiveData.value?.filter {
                when (filterType.type) {
                    DrinkFilterType.ALCOHOL -> it.alcoholType == filterType
                    DrinkFilterType.CATEGORY -> it.category == filterType
                    DrinkFilterType.GLASS -> it.glass == filterType
                    else -> throw IllegalArgumentException("Unknown filter type was chosen")
                }
            }
        }
    }

    private fun applySorting(cocktailSortType: CocktailSortType?) {
        val alcoholDrinkFilter = CocktailAlcoholType.values()
        val alcoholComparator = kotlin.Comparator<CocktailModel> { t, t2 ->
            alcoholDrinkFilter.indexOf(alcoholDrinkFilter.find { it == t.alcoholType }) -
                    alcoholDrinkFilter.indexOf(alcoholDrinkFilter.find { it == t2.alcoholType })
        }

        _cocktailsLiveData.value = _cocktailsLiveData.value?.run {
            when (cocktailSortType ?: CocktailSortType.RECENT) {
                CocktailSortType.RECENT -> sortedByDescending { it.dateAdded }
                CocktailSortType.NAME_DESC -> sortedByDescending { it.names.defaults }
                CocktailSortType.NAME_ASC -> sortedBy { it.names.defaults }
                CocktailSortType.ALCOHOL_FIRST -> sortedWith(nullsLast(alcoholComparator))
                CocktailSortType.NON_ALCOHOL_FIRST -> sortedWith(nullsLast(alcoholComparator)).reversed()
                CocktailSortType.INGREDIENT_DESC -> sortedByDescending { it.ingredients.size }
                CocktailSortType.INGREDIENT_ASC -> sortedBy { it.ingredients.size }
            }
        }
    }
}