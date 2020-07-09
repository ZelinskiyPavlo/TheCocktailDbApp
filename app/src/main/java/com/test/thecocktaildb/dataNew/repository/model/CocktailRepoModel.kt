package com.test.thecocktaildb.dataNew.repository.model

data class CocktailRepoModel(
    val id: Long = -1L,
    val names: LocalizedStringRepoModel = LocalizedStringRepoModel(),
    val category: String = "",
    val alcoholType: String = "",
    val glass: String = "",
    val image: String = "",
    val instructions: LocalizedStringRepoModel = LocalizedStringRepoModel(),
    val ingredients: List<String> = emptyList(),
    val measures: List<String> = emptyList()/*,
    val date: Date = Date()*/
)