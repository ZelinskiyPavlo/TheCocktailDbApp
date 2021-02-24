package com.test.tabhost.communication

import com.test.cocktail.api.CocktailCommunicationApi
import com.test.tabhost.api.TabHostCommunicationApi
import javax.inject.Inject

internal class CocktailCommunicationImpl @Inject constructor(
    tabHostCommunicationApi: TabHostCommunicationApi
): CocktailCommunicationApi {

    override val cocktailWithIdNotFoundEvent = tabHostCommunicationApi.cocktailWithIdNotFoundEvent
}