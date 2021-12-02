package com.test.cocktail.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.test.cocktail.model.sorttype.CocktailSortType
import com.test.presentation.adapter.binding.Page
import com.test.presentation.extension.getStateFlow
import com.test.presentation.mapper.cocktail.CocktailModelMapper
import com.test.presentation.mapper.user.UserModelMapper
import com.test.presentation.model.cocktail.CocktailModel
import com.test.presentation.model.cocktail.filter.DrinkFilter
import com.test.presentation.model.cocktail.filter.DrinkFilterType
import com.test.presentation.model.cocktail.type.CocktailAlcoholType
import com.test.presentation.model.cocktail.type.CocktailCategory
import com.test.presentation.model.cocktail.type.CocktailGlassType
import com.test.presentation.ui.base.BaseViewModel
import com.test.presentation.util.WhileViewSubscribed
import com.test.presentation.util.stateHandle
import com.test.repository.source.CocktailRepository
import com.test.repository.source.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

private const val EXTRA_KEY_CURRENT_PAGE = "EXTRA_KEY_CURRENT_PAGE"
private const val EXTRA_KEY_SORTING = "EXTRA_KEY_SORTING"
private const val EXTRA_KEY_FILTER = "EXTRA_KEY_FILTER"

class CocktailViewModel(
    handle: SavedStateHandle,
    private val cocktailRepo: CocktailRepository,
    userRepo: UserRepository,
    private val cocktailMapper: CocktailModelMapper,
    private val userMapper: UserModelMapper
) : BaseViewModel(handle) {

    sealed class Event {
        class ToDetails(val cocktailId: Long) : Event()

        class ApplyFilter(
            val selectedFiltersList: List<String>,
            val selectedFiltersTypeList: List<String>
        ) : Event()

        // TODO: 21.10.2021 Maybe all related to analytics move to separate event (if it will properly work with Channels)
        //  or even better, move to interface that will be injected in constructor
        //  or even better using delegate, see IoShed UPD: maybe it's pattern from MVI
        class CocktailFavoriteStateChanged(
            val isAddedToFavorite: Boolean,
            val cocktailId: Long,
            val fullUserName: String
        ) : Event()
    }

    private val _eventsChannel = Channel<Event>(capacity = Channel.CONFLATED)
    val eventsFlow = _eventsChannel.receiveAsFlow()
        .shareIn(viewModelScope, WhileViewSubscribed)

    private val cocktailsDbListFlow: Flow<List<CocktailModel>> =
        cocktailRepo.cocktailListFlow.map(cocktailMapper::mapToList)

    private val userFlow = userRepo.userFlow.map {
        it?.run(userMapper::mapTo)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _filtersFlow = MutableStateFlow<List<DrinkFilter?>?>(null)
    val filtersFlow = _filtersFlow.asStateFlow()

    private val _sortingOrderFlow = MutableStateFlow<CocktailSortType?>(null)
    internal val sortingOrderFlow = _sortingOrderFlow.asStateFlow()

    private val _cocktailsFlow = combineTransform(
        cocktailsDbListFlow,
        _filtersFlow,
        _sortingOrderFlow
    ) { cocktailsList, filtersList, sortType ->
        emit(cocktailsList.applyFilter(filtersList).applySorting(sortType))
    }.shareIn(viewModelScope, WhileViewSubscribed)
    val cocktailsFlow = _cocktailsFlow.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    val favoriteCocktailsFlow = _cocktailsFlow
        .map { it.filter { cocktail -> cocktail.isFavorite } }
        .stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    val isCocktailsEmptyFlow = cocktailsFlow.map { it.isEmpty() }
        .stateIn(viewModelScope, WhileViewSubscribed, true)

    val isFavoriteCocktailsEmptyFlow = favoriteCocktailsFlow.map { it.isEmpty() }
        .stateIn(viewModelScope, WhileViewSubscribed, true)

    val filterResultFlow = _cocktailsFlow.map {
        val numberOfHistory = it.size
        val numberOfFavorite = it.filter { item -> item.isFavorite }.size

        if (numberOfHistory == 0 && numberOfFavorite == 0) {
            null
        } else {
            numberOfHistory to numberOfFavorite
        }
    }.shareIn(viewModelScope, SharingStarted.Eagerly)

    val alcoholSignFlow = _filtersFlow.map {
        it?.get(0)?.toString()?.replace("\\/", "")
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    val categorySignFlow = _filtersFlow.map {
        it?.get(1)?.toString()?.replace("\\/", "")
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    val glassSignFlow = _filtersFlow.map {
        it?.get(2)?.toString()?.replace("\\/", "")
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    val currentPageFlow =
        savedStateHandle.getStateFlow(viewModelScope, EXTRA_KEY_CURRENT_PAGE, Page.HistoryPage)

    private var sortingOrderIndex by stateHandle<Int?>(EXTRA_KEY_SORTING)
    private var filterTypeIndexArray by stateHandle<Array<String?>>(EXTRA_KEY_FILTER)

    init {
        restoreSorting()
        restoreFilters()

        sortingOrderFlow.onEach {
            saveSorting(it)
        }.launchIn(viewModelScope)
        filtersFlow.onEach {
            saveFilters(it)
        }.launchIn(viewModelScope)
    }

    fun updateCocktailAndNavigateDetailsFragment(cocktail: CocktailModel) {
        launchRequest {
            cocktailRepo.updateCocktailDate(cocktail.id)
            _eventsChannel.trySend(Event.ToDetails(cocktail.id))
        }
    }

    fun changeIsFavoriteState(cocktail: CocktailModel) {
        launchRequest {
            cocktailRepo.updateCocktailFavoriteState(cocktail.id, cocktail.isFavorite.not())

            val isAddedToFavorite = cocktail.isFavorite.not()
            val cocktailId = cocktail.id
            val userModel = userFlow.value
            val fullUserName = if (userModel != null) {
                "${userModel.name} ${userModel.lastName}"
            } else {
                "Empty userName"
            }

            _eventsChannel.trySend(
                Event.CocktailFavoriteStateChanged(
                    isAddedToFavorite,
                    cocktailId,
                    fullUserName
                )
            )
        }
    }

    fun removeCocktail(cocktail: CocktailModel) {
        launchRequest {
            cocktailRepo.deleteCocktails(cocktail.run(cocktailMapper::mapFrom))
        }
    }

    fun onApplyButtonClicked() {
        val filterList = _filtersFlow.value
        if (filterList != null || filterList == listOf(null, null, null)) {
            val selectedFiltersList = filterList.map { it?.key ?: "None" }
            val selectedFiltersTypeList = filterList.filterNotNull().map { it.type.name }
            _eventsChannel.trySend(Event.ApplyFilter(selectedFiltersList, selectedFiltersTypeList))
        } else {
            _eventsChannel.trySend(Event.ApplyFilter(emptyList(), emptyList()))
        }
    }

    fun openProposedCocktail(selectedCocktailId: Long?) {
        if (cocktailsFlow.value.size > 1) {
            val otherCocktail = cocktailsFlow.value.filter { it.id != selectedCocktailId }.random()
            updateCocktailAndNavigateDetailsFragment(otherCocktail)
        }
    }

    fun addFilter(itemId: Int, filterType: DrinkFilterType) {
        val selectedFilterEnumList = when (filterType) {
            DrinkFilterType.ALCOHOL -> CocktailAlcoholType.values()
            DrinkFilterType.CATEGORY -> CocktailCategory.values()
            DrinkFilterType.GLASS -> CocktailGlassType.values()
            else -> throw IllegalArgumentException("Unknown filter type was chosen")
        }

        _filtersFlow.value =
            (_filtersFlow.value ?: listOf(null, null, null)).toMutableList().apply {
                set(filterType.ordinal, selectedFilterEnumList[itemId])
            }
    }

    internal fun setSorting(newSorting: CocktailSortType?) {
        _sortingOrderFlow.value = newSorting
    }

    fun resetFilters() {
        _filtersFlow.value = null
    }

    fun resetSorting() {
        _sortingOrderFlow.value = null
    }

    private fun List<CocktailModel>.applyFilter(filterTypeList: List<DrinkFilter?>?): List<CocktailModel> {
        if (filterTypeList == null) return this

        var currentList = this
        filterTypeList.filterNotNull().forEach { filterType ->
            currentList = currentList.filter {
                when (filterType.type) {
                    DrinkFilterType.ALCOHOL -> it.alcoholType == filterType

                    DrinkFilterType.CATEGORY -> it.category == filterType

                    DrinkFilterType.GLASS -> it.glass == filterType

                    else -> throw IllegalArgumentException("Unknown filter type was chosen")
                }
            }
        }
        return currentList
    }

    private fun List<CocktailModel>.applySorting(cocktailSortType: CocktailSortType?): List<CocktailModel> {
        if (isEmpty()) {
            return this
        }
        val alcoholDrinkFilter = CocktailAlcoholType.values()
        val alcoholComparator = kotlin.Comparator<CocktailModel> { t, t2 ->
            alcoholDrinkFilter.indexOf(alcoholDrinkFilter.find { it == t.alcoholType }) -
                    alcoholDrinkFilter.indexOf(alcoholDrinkFilter.find { it == t2.alcoholType })
        }

        return when (cocktailSortType ?: CocktailSortType.RECENT) {
            CocktailSortType.RECENT -> sortedByDescending { it.dateAdded }

            CocktailSortType.NAME_DESC -> sortedByDescending { it.names.defaults }

            CocktailSortType.NAME_ASC -> sortedBy { it.names.defaults }

            CocktailSortType.ALCOHOL_FIRST -> sortedWith(nullsLast(alcoholComparator))

            CocktailSortType.NON_ALCOHOL_FIRST -> sortedWith(nullsLast(alcoholComparator)).reversed()

            CocktailSortType.INGREDIENT_DESC -> sortedByDescending { it.ingredients.size }

            CocktailSortType.INGREDIENT_ASC -> sortedBy { it.ingredients.size }
        }
    }

    private fun saveFilters(currentFilters: List<DrinkFilter?>?) {
        currentFilters?.forEachIndexed { index, filterType ->
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

    private fun saveSorting(currentSorting: CocktailSortType?) {
        sortingOrderIndex = currentSorting?.ordinal ?: 0
    }

    private fun restoreSorting() {
        sortingOrderIndex?.let {
            _sortingOrderFlow.value = CocktailSortType.values()[it]
        }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    private fun restoreFilters() {
        if (filterTypeIndexArray == null) {
            _filtersFlow.value = null
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
            _filtersFlow.value = extractedFilterList
        }
    }
}