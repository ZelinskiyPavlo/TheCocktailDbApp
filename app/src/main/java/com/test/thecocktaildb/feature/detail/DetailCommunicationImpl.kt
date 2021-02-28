package com.test.thecocktaildb.feature.detail

import com.test.common.Event
import com.test.detail.api.DetailCommunicationApi
import com.test.repository.source.EventRepository
import javax.inject.Inject

class DetailCommunicationImpl @Inject constructor(
    private val eventRepository: EventRepository
): DetailCommunicationApi {

    override fun sendNoCocktailWithIdFoundEvent() {
        eventRepository.noCocktailWithIdFoundEvent.value = Event(Unit)
    }
}