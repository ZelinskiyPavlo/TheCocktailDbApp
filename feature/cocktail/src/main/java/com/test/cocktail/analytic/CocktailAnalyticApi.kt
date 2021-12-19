package com.test.cocktail.analytic

interface CocktailAnalyticApi {

    fun logCocktailFilterApply(
        selectedFiltersList: List<String>,
        selectedFiltersTypeList: List<String>
    )

    fun logFavoriteCocktailStateChanged(
        isAddedToFavorite: Boolean,
        cocktailId: String,
        fullUserName: String
    )
}