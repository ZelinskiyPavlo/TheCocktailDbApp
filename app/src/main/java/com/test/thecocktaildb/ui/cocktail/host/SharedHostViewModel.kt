package com.test.thecocktaildb.ui.cocktail.host

import androidx.lifecycle.*
import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.cocktail.filtertype.*
import com.test.thecocktaildb.ui.cocktail.sorttype.CocktailSortType
import com.test.thecocktaildb.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class SharedHostViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel() {

    private val _cocktailsLiveData = MutableLiveData<List<Cocktail>>().apply { value = emptyList() }
    val cocktailsLiveData: LiveData<List<Cocktail>> = _cocktailsLiveData

    val favoriteListLiveData: LiveData<List<Cocktail>> = Transformations.map(_cocktailsLiveData) {
        it.filter { cocktail -> cocktail.isFavorite }
    }

    private var allCocktailList: List<Cocktail>? = null

    private val _cocktailDetailsEventLiveData = MutableLiveData<Event<Pair<String, String>>>()
    val cocktailDetailsEventLiveData: LiveData<Event<Pair<String, String>>> = _cocktailDetailsEventLiveData

    private val _applyFilterEventLiveData = MutableLiveData<Event<Unit>>()
    val applyFilterEventLiveData: LiveData<Event<Unit>> = _applyFilterEventLiveData

    val isCocktailListEmptyLiveData: LiveData<Boolean> =
        Transformations.map(_cocktailsLiveData) { it.isEmpty() }

    val isFavoriteListEmptyLiveData: LiveData<Boolean> =
        Transformations.map(favoriteListLiveData) { it.isEmpty() }

    private val _filterListLiveData = MediatorLiveData<List<DrinkFilter?>>()
        .apply { value = listOf<DrinkFilter?>(null, null, null) }
    val filterListLiveData: LiveData<List<DrinkFilter?>> = _filterListLiveData

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
    var sortingOrderLiveData = MutableLiveData<CocktailSortType?>()

    private val filterAndSortLiveData: LiveData<Unit> = MediatorLiveData<Unit>().apply {
        fun transformData() {
            allCocktailList?.let { _cocktailsLiveData.value = it }
            applyFilter(_filterListLiveData.value ?: listOf(null, null, null))
            applySorting(sortingOrderLiveData.value)
            value = Unit
        }

        addSource(_filterListLiveData) {
            transformData()
        }

        addSource(sortingOrderLiveData) {
            transformData()
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

    fun loadCocktails() {
        disposable.add(
            repository.getCocktails().subscribeBy(onSuccess = { cocktailsList ->
                _cocktailsLiveData.value = cocktailsList
                allCocktailList = _cocktailsLiveData.value
            }, onError = { Timber.e("Error occurred when loading cocktails, $it") })
        )
    }

    fun updateCocktailAndNavigateDetailsFragment(cocktail: Cocktail) {
        cocktail.dateAdded = Calendar.getInstance().time
        disposable.add(
            repository.saveCocktail(cocktail)
                .subscribeBy(onComplete = {
                    navigateToCocktailDetailsFragment(cocktail)
                }, onError = { Timber.e("Error occurred when updating cocktail, $it") })
        )
    }

    private fun navigateToCocktailDetailsFragment(cocktail: Cocktail) {
        _cocktailDetailsEventLiveData.value = Event(Pair(cocktail.strDrink, cocktail.idDrink))
    }

    fun openProposedCocktail(selectedCocktailId: String?) {
        val otherCocktail = cocktailsLiveData.value
            ?.filter { it.idDrink != selectedCocktailId }?.random()

        if (otherCocktail != null) {
            updateCocktailAndNavigateDetailsFragment(otherCocktail)
        }
    }

    fun changeIsFavoriteState(cocktail: Cocktail) {
        disposable.add(
            repository.updateFavoriteState(cocktail.idDrink, cocktail.isFavorite.not())
                .doOnComplete {
                    Timber.i("Favorite cocktail added to Db")
                }
                .subscribeBy(onComplete = {
                    val cocktailCopy = cocktail.copy(isFavorite = cocktail.isFavorite.not())
                    val updatedCocktailList = allCocktailList?.toMutableList()?.map {
                        if (it.idDrink == cocktailCopy.idDrink) cocktailCopy
                        else it
                    }
                    _cocktailsLiveData.value = updatedCocktailList
                    allCocktailList = updatedCocktailList
                })
        )
    }

    fun setInitialText(chooseText: String, changeText: String, emptyResultText: String) {
        changeTextSuffix = changeText
        chooseTextSuffix = chooseText

        emptyResult = emptyResultText

        _filterListLiveData.value = listOf(null, null, null)
    }

    fun filterSpecified(itemId: Int, filterType: DrinkFilterType) {
        _filterListLiveData.value = when (filterType) {
            DrinkFilterType.ALCOHOL -> {
                _filterListLiveData.value?.toMutableList()?.apply {
                    set(0, AlcoholDrinkFilter.values()[itemId])
                }
            }
            DrinkFilterType.CATEGORY -> {
                _filterListLiveData.value?.toMutableList()?.apply {
                    set(1, CategoryDrinkFilter.values()[itemId])
                }
            }
            DrinkFilterType.INGREDIENT -> {
                _filterListLiveData.value?.toMutableList()?.apply {
                    set(2, CocktailIngredient.values()[itemId])
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
                        _cocktailsLiveData.value?.filter { it.strAlcoholic == filterType.key }
                }
                DrinkFilterType.CATEGORY -> {
                    _cocktailsLiveData.value =
                        _cocktailsLiveData.value?.filter { it.strCategory == filterType.key }
                }
                DrinkFilterType.INGREDIENT -> {
                    _cocktailsLiveData.value =
                        _cocktailsLiveData.value?.filter {
                            it.createIngredientsList()
                                .map { ingredient -> ingredient.name }
                                .contains(filterType.key)
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

        val alcoholDrinkFilter = AlcoholDrinkFilter.values()
        val alcoholComparator = kotlin.Comparator<Cocktail> { t, t2 ->
            if (t.strAlcoholic != null && t2.strAlcoholic != null)
                alcoholDrinkFilter.indexOf(alcoholDrinkFilter.find { it.key == t.strAlcoholic }) -
                        alcoholDrinkFilter
                            .indexOf(alcoholDrinkFilter.find { it.key == t2.strAlcoholic })
            else 0
        }

        _cocktailsLiveData.value = when (cachedSortingOrder) {
            CocktailSortType.RECENT ->
                _cocktailsLiveData.value?.sortedByDescending { it.dateAdded }
            CocktailSortType.NAME_DESC ->
                _cocktailsLiveData.value?.sortedByDescending { it.strDrink }
            CocktailSortType.NAME_ASC ->
                _cocktailsLiveData.value?.sortedBy { it.strDrink }
            CocktailSortType.ALCOHOL_FIRST ->
                _cocktailsLiveData.value?.sortedWith(nullsLast(alcoholComparator))
            CocktailSortType.NON_ALCOHOL_FIRST ->
                _cocktailsLiveData.value?.sortedWith(nullsLast(alcoholComparator))?.reversed()
            CocktailSortType.INGREDIENT_DESC ->
                _cocktailsLiveData.value?.sortedByDescending { it.ingredientsNumber() }
            CocktailSortType.INGREDIENT_ASC ->
                _cocktailsLiveData.value?.sortedBy { it.ingredientsNumber() }
            null -> _cocktailsLiveData.value?.sortedByDescending { it.dateAdded }
        }
    }
}