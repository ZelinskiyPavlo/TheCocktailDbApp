package com.test.thecocktaildb.ui.cocktailsScreen.fragmentHostScreen

import androidx.lifecycle.*
import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.AlcoholDrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.CategoryDrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilterType
import com.test.thecocktaildb.ui.cocktailsScreen.sortType.CocktailSortType
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

    private val _cocktailDetailsEvent = MutableLiveData<Event<Pair<String, String>>>()
    val cocktailDetailsEvent: LiveData<Event<Pair<String, String>>> = _cocktailDetailsEvent

    private val _applyFilterEventLiveData = MutableLiveData<Event<Unit>>()
    val applyFilterEventLiveData: LiveData<Event<Unit>> = _applyFilterEventLiveData

    val isCocktailListEmpty: LiveData<Boolean> =
        Transformations.map(_cocktailsLiveData) { it.isEmpty() }

    val isFavoriteListEmpty: LiveData<Boolean> =
        Transformations.map(favoriteListLiveData) { it.isEmpty() }

    private val _alcoholFilterLiveData = MutableLiveData<DrinkFilter>()
    private val _categoryFilterLiveData = MutableLiveData<DrinkFilter>()

    val alcoholSignLiveData: LiveData<String> = Transformations.map(_alcoholFilterLiveData) {
        if (it == null) chooseTextSuffix
        else "${it.key}    $changeTextSuffix"
    }

    val categorySignLiveData: LiveData<String> = Transformations.map(_categoryFilterLiveData) {
        if (it == null) chooseTextSuffix
        else "${it.key}    $changeTextSuffix"
    }

    private lateinit var changeTextSuffix: String
    private lateinit var chooseTextSuffix: String

    private var defaultItemOrder: List<Cocktail>? = null

//    private var cachedFilterTypeList: List<DrinkFilter?> = listOf(null)
    private var cachedSortingOrder: CocktailSortType? = null

    val filterResultMediatorLiveData: LiveData<String> =
        MediatorLiveData<String>().apply {
            fun determineResult() {
                val numberOfHistory = cocktailsLiveData.value?.size
                val numberOfFavorite = favoriteListLiveData.value?.size
                Timber.i("alcohol ${_alcoholFilterLiveData.value} and category ${_categoryFilterLiveData.value}")
                if(_alcoholFilterLiveData.value == null && _categoryFilterLiveData.value == null)
                    return
                value = if (numberOfHistory == 0 && numberOfFavorite == 0)
                    "Результаті відсутні"
                else
                    "Результати (${numberOfHistory ?: 0}⌚, ${numberOfFavorite ?: 0}♥)"
            }
            addSource(_alcoholFilterLiveData) {
                applyFilter(it)
                Timber.i("_alcoholFilterLiveData called")
                if(_alcoholFilterLiveData.value != null)
                determineResult()
            }
            addSource(_categoryFilterLiveData) {
                applyFilter(it)
                Timber.i("_categoryFilterLiveData called")
                if(_categoryFilterLiveData.value != null)
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
                defaultItemOrder = cocktailsLiveData.value
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
        _cocktailDetailsEvent.value = Event(Pair(cocktail.strDrink, cocktail.idDrink))
    }

    fun openProposedCocktail(selectedCocktailId: String?) {
        val otherCocktail = cocktailsLiveData.value
            ?.filter { it.idDrink != selectedCocktailId }?.random()

        if (otherCocktail != null) {
            updateCocktailAndNavigateDetailsFragment(otherCocktail)
        }
    }

    fun changeIsFavoriteState(cocktail: Cocktail, state: Boolean) {
        disposable.add(
            repository.updateFavoriteState(cocktail.idDrink, true)
                .doOnComplete {
                    Timber.i("Favorite cocktail added to Db")
                }
                .subscribeBy(onComplete = {
                    val cocktailCopy = cocktail.copy(isFavorite = state)
                    val updatedCocktailList = allCocktailList?.toMutableList()?.map {
                        if (it.idDrink == cocktailCopy.idDrink) cocktailCopy
                        else it
                    }
                    _cocktailsLiveData.value = updatedCocktailList
                    allCocktailList = updatedCocktailList
                })
        )
    }

    fun setInitialText(chooseText: String, changeText: String) {
        changeTextSuffix = changeText
        chooseTextSuffix = chooseText

        _alcoholFilterLiveData.value = null
        _categoryFilterLiveData.value = null
    }

    fun alcoholFilterSpecified(itemId: Int) {
        _alcoholFilterLiveData.value = AlcoholDrinkFilter.values()[itemId]
    }

    fun categoryFilterSpecified(itemId: Int) {
        _categoryFilterLiveData.value = CategoryDrinkFilter.values()[itemId]
    }

    private fun applyFilter(filterType: DrinkFilter?) {
        if ((_alcoholFilterLiveData.value == null && _categoryFilterLiveData.value == null)
            || filterType == null
        ) {
            _cocktailsLiveData.value = allCocktailList
            applySorting(cachedSortingOrder)
            return
        }

        when (filterType.type) {
            DrinkFilterType.ALCOHOL -> {
                _cocktailsLiveData.value =
                    _cocktailsLiveData.value?.filter { it.strAlcoholic == filterType.key }
            }
            DrinkFilterType.CATEGORY -> {
                _cocktailsLiveData.value =
                    _cocktailsLiveData.value?.filter { it.strCategory == filterType.key }
            }
            else -> throw IllegalArgumentException("unknown filter type was chosen")
        }
    }

    fun resetFilters() {
        _alcoholFilterLiveData.value = null
        _categoryFilterLiveData.value = null
    }

    fun onApplyButtonClicked() {
        _applyFilterEventLiveData.value = Event(Unit)
    }

    fun applySorting(cocktailSortType: CocktailSortType?) {
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