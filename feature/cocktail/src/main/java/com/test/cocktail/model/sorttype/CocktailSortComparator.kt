package com.test.cocktail.model.sorttype

class CocktailSortComparator {

    val alcoholComparator = kotlin.Comparator<String> { t, t2 ->
        CocktailSortType.values()
            .indexOf(CocktailSortType.values().find { it.key == t }) -
                CocktailSortType.values().indexOf(
                    CocktailSortType.values().find { it.key == t2 })
    }

    val reverseAlcoholComparator = kotlin.Comparator<String> { t, t2 ->
        CocktailSortType.values()
            .indexOf(CocktailSortType.values().find { it.key == t2 }) -
                CocktailSortType.values().indexOf(
                    CocktailSortType.values().find { it.key == t })
    }
}