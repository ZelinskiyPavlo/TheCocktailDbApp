package com.test.thecocktaildb.feature.tabhost

import androidx.lifecycle.LiveData
import com.test.common.Event
import com.test.repository.source.EventRepository
import com.test.tabhost.api.TabHostCommunicationApi
import javax.inject.Inject

class TabHostCommunicationImpl @Inject constructor(
    eventRepository: EventRepository
): TabHostCommunicationApi {

    override val cocktailWithIdNotFoundEvent: LiveData<Event<Unit>> =
        eventRepository.noCocktailWithIdFoundEvent
}