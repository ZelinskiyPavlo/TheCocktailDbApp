package com.test.thecocktaildb.ui.cocktailsScreen.favoriteScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.data.AppCocktailsRepository
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.cocktailsScreen.AdapterHandler
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.AlcoholDrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilterType
import com.test.thecocktaildb.ui.cocktailsScreen.sortType.CocktailSortType
import com.test.thecocktaildb.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val repository: AppCocktailsRepository) :
    ViewModel(), AdapterHandler {

    private val _items = MutableLiveData<List<Cocktail>>().apply { value = emptyList() }
    val items: LiveData<List<Cocktail>> = _items

    private val _cocktailDetailsEvent = MutableLiveData<Event<Pair<String, String>>>()
    val cocktailDetailsEvent: LiveData<Event<Pair<String, String>>> = _cocktailDetailsEvent

    val isSearchResultEmpty: LiveData<Boolean> = Transformations.map(_items) { it.isNullOrEmpty() }

    private var allCocktailList: List<Cocktail>? = null
    private var defaultItemOrder: List<Cocktail>? = null

    private var cachedFilterTypeList: List<DrinkFilter?> = listOf(null)
    private var cachedSortingOrder: CocktailSortType? = null

    private var isFiltered: Boolean = false
    private var isSorted: Boolean = false

    private val disposable = CompositeDisposable()

    override fun onCleared() = disposable.clear()

    fun loadFavoriteCocktails() {
        disposable.add(
            repository.getCocktails().subscribeBy(onSuccess = { cocktailsList ->
                _items.value = cocktailsList.filter { it.isFavorite }
                allCocktailList = _items.value
            }, onError = { Timber.e("Error occurred when loading cocktails, $it") })
        )
    }

    override fun updateCocktailAndNavigateDetailsFragment(cocktail: Cocktail) {
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

    fun applyFilter(filterTypeList: List<DrinkFilter?>, forceFilter: Boolean = false) {
        cachedFilterTypeList = filterTypeList
        isFiltered = true

        if (!forceFilter) _items.value = allCocktailList

        filterTypeList.forEach { drinkFilter ->
            if (drinkFilter != null) {
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
            } else {
                isFiltered = false
            }
        }
        if (isSorted) {
            isFiltered = false
            applySorting(cachedSortingOrder, true)
        }
    }

    override fun addCocktailToFavorite(cocktail: Cocktail) {
    }

    override fun removeCocktailFromFavorite(cocktail: Cocktail) {
        disposable.add(
            repository.updateFavoriteState(cocktail.idDrink, false)
                .doOnComplete {
                    Timber.i("Cocktail removed from favorite")
                }
                .subscribeBy(onComplete = {
                    val updatedCocktailList = allCocktailList?.toMutableList()?.apply {
                        removeIf { it.idDrink == cocktail.idDrink }
                    }
                    allCocktailList = updatedCocktailList
                    applyFilter(cachedFilterTypeList)
                })
        )
    }

    fun updateFavoriteList(cocktail: Cocktail) {
        val updatedCocktailList = allCocktailList?.toMutableList()
        if (updatedCocktailList?.contains(cocktail)
                ?.not() != false || updatedCocktailList.isNullOrEmpty()
        )
            updatedCocktailList?.add(cocktail)
        allCocktailList = updatedCocktailList?.sortedByDescending { it.dateAdded }
        applyFilter(cachedFilterTypeList)
    }

    fun applySorting(cocktailSortType: CocktailSortType?, forceSorting: Boolean = false) {
        cachedSortingOrder = cocktailSortType ?: CocktailSortType.RECENT
        isSorted = true

        if (!forceSorting) _items.value = defaultItemOrder

        val alcoholDrinkFilter = AlcoholDrinkFilter.values()
        val alcoholComparator = kotlin.Comparator<Cocktail> { t, t2 ->
            if (t.strAlcoholic != null && t2.strAlcoholic != null)
                alcoholDrinkFilter.indexOf(alcoholDrinkFilter.find { it.key == t.strAlcoholic }) -
                        alcoholDrinkFilter
                            .indexOf(alcoholDrinkFilter.find { it.key == t2.strAlcoholic })
            else 0
        }

        _items.value = when (cachedSortingOrder) {
            CocktailSortType.RECENT -> {
                isSorted = false
                _items.value?.sortedByDescending { it.dateAdded }
            }
            CocktailSortType.NAME_DESC ->
                _items.value?.sortedByDescending { it.strDrink }
            CocktailSortType.NAME_ASC ->
                _items.value?.sortedBy { it.strDrink }
            CocktailSortType.ALCOHOL_FIRST ->
                _items.value?.sortedWith(nullsLast(alcoholComparator))
            CocktailSortType.NON_ALCOHOL_FIRST ->
                _items.value?.sortedWith(nullsLast(alcoholComparator))?.reversed()
            CocktailSortType.INGREDIENT_DESC ->
                _items.value?.sortedByDescending { it.ingredientsNumber() }
            CocktailSortType.INGREDIENT_ASC ->
                _items.value?.sortedBy { it.ingredientsNumber() }
            null -> _items.value?.sortedByDescending { it.dateAdded }
        }
        if (isFiltered) {
            isSorted = false
            applyFilter(cachedFilterTypeList, true)
        }
    }
}