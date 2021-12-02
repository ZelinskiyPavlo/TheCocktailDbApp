package com.test.cocktail.api

import kotlinx.coroutines.flow.Flow

interface CocktailCommunicationApi {

    val cocktailWithIdNotFoundFlow: Flow<Unit>
}