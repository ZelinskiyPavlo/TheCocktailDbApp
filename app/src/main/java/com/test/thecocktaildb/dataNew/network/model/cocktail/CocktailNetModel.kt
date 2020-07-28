package com.test.thecocktaildb.dataNew.network.model.cocktail

data class CocktailNetModel(
    val id: Long = -1L,
    val names: LocalizedStringNetModel = LocalizedStringNetModel(),
    val category: String = "",
    val alcoholType: String = "",
    val glass: String = "",
    val image: String = "",
    val instructions: LocalizedStringNetModel = LocalizedStringNetModel(),
    val ingredients: List<String> = emptyList(),
    val measures: List<String> = emptyList(),
)