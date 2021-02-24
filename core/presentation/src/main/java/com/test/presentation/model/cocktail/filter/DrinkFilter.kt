package com.test.presentation.model.cocktail.filter

interface DrinkFilter {
    val type: DrinkFilterType
    val key: String
}