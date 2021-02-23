package com.test.cocktail.api

import androidx.lifecycle.LiveData
import com.test.common.Event

interface CocktailCommunicationApi {

    val cocktailWithIdNotFoundEvent: LiveData<Event<Unit>>
}