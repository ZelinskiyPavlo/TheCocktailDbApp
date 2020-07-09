package com.test.thecocktaildb.presentationNew.model

@Suppress("unused")
enum class CocktailAlcoholType(val key: String) {
    ALCOHOLIC("Alcoholic"),
    NON_ALCOHOLIC("Non alcoholic"),
    OPTIONAL_ALCOHOL("Optional alcohol"),
    UNDEFINED("")
}