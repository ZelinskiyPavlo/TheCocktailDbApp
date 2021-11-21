package com.test.thecocktaildb.feature.tabhost

import com.test.repository.source.EventRepository
import com.test.tabhost.api.TabHostCommunicationApi
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class TabHostCommunicationImpl @Inject constructor(
    eventRepository: EventRepository
): TabHostCommunicationApi {

    override val cocktailWithIdNotFoundFlow =
        eventRepository.noCocktailWithIdFoundChannel.receiveAsFlow()
}