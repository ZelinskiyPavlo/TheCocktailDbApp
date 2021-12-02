package com.test.thecocktaildb.feature.detail

import com.test.detail.api.DetailCommunicationApi
import com.test.repository.source.EventRepository
import javax.inject.Inject

class DetailCommunicationImpl @Inject constructor(
    private val eventRepository: EventRepository
): DetailCommunicationApi {

    override fun sendNoCocktailWithIdFoundEvent() {
        eventRepository.noCocktailWithIdFoundChannel.trySend(Unit)
    }
}