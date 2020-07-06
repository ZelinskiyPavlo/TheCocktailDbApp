package com.test.thecocktaildb.ui.cocktailScreen.sortType

enum class CocktailSortType(override val key: String): SortType {
    RECENT("Recent"),
    NAME_DESC("Name descending"),
    NAME_ASC("Name ascending"),
    ALCOHOL_FIRST("Alcohol first"),
    NON_ALCOHOL_FIRST("Non alcohol first"),
    INGREDIENT_DESC("Ingredient count descending"),
    INGREDIENT_ASC("Ingredient count ascending"),
}