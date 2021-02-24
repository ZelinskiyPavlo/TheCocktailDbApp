package com.test.cocktail.model.sorttype

import com.test.presentation.model.cocktail.type.CocktailAlcoholType

class CocktailSortComparator {

    val alcoholComparator = kotlin.Comparator<String> { t, t2 ->
        CocktailAlcoholType.values()
            .indexOf(CocktailAlcoholType.values().find { it.key == t }) -
                CocktailAlcoholType.values().indexOf(
                    CocktailAlcoholType.values().find { it.key == t2 })
    }

    val reverseAlcoholComparator = kotlin.Comparator<String> { t, t2 ->
        CocktailAlcoholType.values()
            .indexOf(CocktailAlcoholType.values().find { it.key == t2 }) -
                CocktailAlcoholType.values().indexOf(
                    CocktailAlcoholType.values().find { it.key == t })
    }
}